package com.hana.ddok.users.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.account.util.AccountNumberGenerator;
import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.common.exception.ValueInvalidException;
import com.hana.ddok.common.jwt.JWTUtil;
import com.hana.ddok.home.domain.Home;
import com.hana.ddok.home.repository.HomeRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.dto.*;
import com.hana.ddok.users.exception.UsersInvalidPwd;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final HomeRepository homeRepository;
    private final AccountRepository accountRepository;
    private final ProductsRepository productsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final DefaultMessageService messageService;

    @Value("${jwt.expired.time}")
    private Long expiredTime;
    @Value("${coolsms.number.from}")
    private String from;

    public UsersLoginRes usersLogin(UsersLoginReq req) {

        Users user = usersRepository.findByPhoneNumber(req.phoneNumber());
        if (user == null) {
            throw new UsersNotFound();
        }
        String targetPwd = req.password();
        String originPwd = user.getPassword();

        if (!bCryptPasswordEncoder.matches(targetPwd, originPwd)) {
            throw new UsersInvalidPwd();
        }

        String token = jwtUtil.createJwt(user.getPhoneNumber(), expiredTime);
        return new UsersLoginRes(true, user.getName(), user.getPhoneNumber(), user.getStep(), user.getStepStatus(), token);
    }


    public UsersJoinRes usersJoin(UsersJoinReq req) {
        if(!req.password().equals(req.confirmPassword()))
            throw new UsersInvalidPwd();

        String encodedPwd = bCryptPasswordEncoder.encode(req.password());
        Home home = homeRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("집을 찾을 수 없습니다"));
        Users users = usersRepository.save(UsersJoinReq.toEntity(req, encodedPwd, home));

        // 입출금계좌 자동 개설
        Products products = productsRepository.findByType(1)
                .orElseThrow(() -> new ProductsNotFound());
        String accountNumber;
        Optional<Account> existingAccount;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
            existingAccount = accountRepository.findByAccountNumber(accountNumber);
        } while (existingAccount.isPresent());
        String password = "1234";
        accountRepository.save(req.toAccount(users, products, accountNumber, password));

        return new UsersJoinRes(true, users.getUsersId(), users.getPhoneNumber());
    }

    public UsersMessageRes usersMessage(UsersMessageReq req) {
        Message message = new Message();

        Random random = new Random();
        Integer code = 1000000 + random.nextInt(9000000);

        message.setFrom(from);
        message.setTo(req.phoneNumber());
        message.setText("인증번호: " + code);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return new UsersMessageRes(code);
    }

    public UsersMsgCheckRes usersMsgCheck(UsersMsgCheckReq req) {
        return new UsersMsgCheckRes(Objects.equals(req.code(), req.input()) ? "match" : "mismatch");
    }

    public UsersGetRes usersGet(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        return new UsersGetRes(user.getName(), user.getPhoneNumber(), user.getStep(), user.getStepStatus());
    }

    public UsersGetPointRes usersGetPoint(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        return new UsersGetPointRes(user.getPoints());
    }

    public UsersSavePointRes usersSavePoint(String phoneNumber, UsersSavePointReq req) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
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

    //null(시작전)
    //1 : 진행중
    //2 : 성공
    //3 : 성공확인
    //4 : 실패
    public UsersMissionRes usersMissionStart(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        user.updateStep(user.getStep()+1);
        user.updateStepStatus(1); //진행중
        usersRepository.save(user);
        return new UsersMissionRes(user.getPhoneNumber(), user.getStep(), user.getStepStatus());
    }


    public UsersMissionRes usersMove(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        Home home = homeRepository.findById(user.getHome().getHomeId() + 1).orElseThrow(() -> new EntityNotFoundException("집을 찾을 수 없습니다."));
        user.updateHome(home);
        user.updateStepStatus(2); //성공
        usersRepository.save(user);
        return new UsersMissionRes(user.getPhoneNumber(), user.getStep(), user.getStepStatus());
    }

    public UsersMissionRes usersMissionCheck(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        user.updateStepStatus(user.getStepStatus() == 2 ? 3 : 1); //성공 -> 성공확인 / 실패 -> 진행중
        usersRepository.save(user);
        return new UsersMissionRes(user.getPhoneNumber(), user.getStep(), user.getStepStatus());
    }

    public UsersReadNewsRes usersReadNews(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        if (user.getReadNews())
            throw new ValueInvalidException("이미 읽은 회원입니다.");
        user.updateReadNews(true);
        usersRepository.save(user);
        return new UsersReadNewsRes(true);
    }
}
