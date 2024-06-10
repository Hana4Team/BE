package com.hana.ddok.users.service;

import com.hana.ddok.account.service.AccountService;
import com.hana.ddok.common.jwt.JWTUtil;
import com.hana.ddok.home.domain.Home;
import com.hana.ddok.home.exception.HomeNotFound;
import com.hana.ddok.home.repository.HomeRepository;
import com.hana.ddok.transaction.service.TransactionService;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.domain.UsersStepStatus;
import com.hana.ddok.users.dto.*;
import com.hana.ddok.users.exception.UsersExistPhoneNumber;
import com.hana.ddok.users.exception.UsersInvalidPwd;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.exception.UsersReadNewsUpdateDenied;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final HomeRepository homeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final DefaultMessageService messageService;

    @Value("${jwt.expired.time}")
    private Long expiredTime;
    @Value("${coolsms.number.from}")
    private String from;

    public UsersLoginRes usersLogin(UsersLoginReq req) {
        Users users = usersRepository.findByPhoneNumber(req.phoneNumber())
                .orElseThrow(() -> new UsersNotFound());
        String targetPwd = req.password();
        String originPwd = users.getPassword();

        if (!bCryptPasswordEncoder.matches(targetPwd, originPwd)) {
            throw new UsersInvalidPwd();
        }

        String token = jwtUtil.createJwt(users.getPhoneNumber(), expiredTime);
        return new UsersLoginRes(true, users, token);
    }

    @Transactional
    public UsersJoinRes usersJoin(UsersJoinReq req) {
        Optional<Users> existingUser = usersRepository.findByPhoneNumber(req.phoneNumber());
        if (existingUser.isPresent()) {
            throw new UsersExistPhoneNumber();
        }

        if(!req.password().equals(req.confirmPassword()))
            throw new UsersInvalidPwd();

        String encodedPwd = bCryptPasswordEncoder.encode(req.password());
        Home home = homeRepository.findById(1L).orElseThrow(() -> new HomeNotFound());
        Users users = usersRepository.save(UsersJoinReq.toEntity(req, encodedPwd, home));

        // 더미데이터 : 입출금계좌 개설 + 소비하기
        accountService.generateDummyDepositWithdrawalAccount(users);
        transactionService.generateDummyTransaction(users);

        return new UsersJoinRes(true, users.getUsersId(), users.getPhoneNumber());
    }

    public UsersMessageRes usersMessage(UsersMessageReq req) {
        Message message = new Message();

        Random random = new Random();
        int codeValue = random.nextInt(10000000); // 0~9999999
        String code = String.format("%07d", codeValue);

        message.setFrom(from);
        message.setTo(req.phoneNumber());
        message.setText("인증번호: " + code);
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return new UsersMessageRes(code);
    }

    public UsersMsgCheckRes usersMsgCheck(UsersMsgCheckReq req) {
        return new UsersMsgCheckRes(Objects.equals(req.code(), req.input()) ? "match" : "mismatch");
    }

    @Transactional(readOnly = true)
    public UsersGetRes usersGet(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        return new UsersGetRes(users);
    }

    @Transactional(readOnly = true)
    public UsersGetPointRes usersGetPoint(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        return new UsersGetPointRes(user.getPoints());
    }

    @Transactional
    public UsersSavePointRes usersSavePoint(String phoneNumber, UsersSavePointReq req) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Random random = new Random();
        Integer points = 0;
        Integer curStep = user.getStep();

        if (req.isMission()) {
            points = switch (user.getStep()) {
                case 1 -> random.nextInt(201) + 100; // 100 ~ 300
                case 2 -> random.nextInt(201) + 500; // 500 ~ 700
                case 3 -> random.nextInt(301) + 700; // 700 ~ 1000
                case 4 -> random.nextInt(1001) + 1000; // 1000 ~ 2000
                case 5 -> random.nextInt(1001) + 2000; // 2000 ~ 3000
                default -> throw new IllegalArgumentException("Invalid step: " + user.getStep());
            };
        } else {
            curStep = 0;
            points = random.nextInt(41) + 10; // 10 ~ 50
        }

        user.updatePoints(user.getPoints() + points);
        usersRepository.save(user);

        return new UsersSavePointRes(curStep, points);
    }

    @Transactional
    public UsersMissionRes usersMissionStart(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        users.updateStepStatus(UsersStepStatus.PROCEEDING);
        usersRepository.save(users);
        return new UsersMissionRes(users);
    }

    @Transactional
    public UsersMissionRes usersMissionCheck(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        if (users.getStepStatus() == UsersStepStatus.FAIL) {
            if (users.getStep() == 2) {
                users.updateStepStatus(UsersStepStatus.PROCEEDING);
            } else users.updateStepStatus(UsersStepStatus.NOTSTARTED);
        } else {
            users.updateStep(users.getStep() + 1);
            users.updateStepStatus(UsersStepStatus.NOTSTARTED);
        }
        usersRepository.save(users);
        return new UsersMissionRes(users);
    }

    @Transactional
    public UsersReadNewsRes usersReadNews(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        if (users.getReadNews())
            throw new UsersReadNewsUpdateDenied();
        users.updateReadNews(true);
        usersRepository.save(users);
        return new UsersReadNewsRes(true);
    }

    @Transactional
    public UsersMissionRes usersMove(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Home home = homeRepository.findById(users.getHome().getHomeId() + 1)
                .orElseThrow(() -> new HomeNotFound());

        users.updateHome(home);
        users.updateStepStatus(UsersStepStatus.SUCCESS);
        usersRepository.save(users);
        return new UsersMissionRes(users);
    }

}
