package com.hana.ddok.users.service;

import com.hana.ddok.common.jwt.JWTUtil;
import com.hana.ddok.home.domain.Home;
import com.hana.ddok.home.repository.HomeRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.domain.UsersDetails;
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

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final HomeRepository homeRepository;
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
        return new UsersLoginRes(user.getName(), user.getPhoneNumber(), user.getStep(), user.getStepStatus(), token);
    }


    public UsersJoinRes usersJoin(UsersJoinReq req) {
        if(!req.password().equals(req.confirmPassword()))
            throw new UsersInvalidPwd();

        String encodedPwd = bCryptPasswordEncoder.encode(req.password());
        Home home = homeRepository.findById(1L).get();
        Users user = usersRepository.save(UsersJoinReq.toEntity(req, encodedPwd, home));

        return new UsersJoinRes(user.getUsersId(), user.getPhoneNumber());
    }

    public SingleMessageSentResponse usersMessage(UsersMessageReq req) {
        Message message = new Message();

        Random random = new Random();
        int code = 1000000 + random.nextInt(9000000);

        message.setFrom(from);
        message.setTo(req.phoneNumber());
        message.setText("인증번호: " + code);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        return response;
    }

    public UsersGetRes usersGet(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        return new UsersGetRes(user.getName(), user.getPhoneNumber(), user.getStep(), user.getStepStatus());
    }

}
