package com.hana.ddok.users.service;

import com.hana.ddok.common.exception.ValueInvalidException;
import com.hana.ddok.home.domain.Home;
import com.hana.ddok.home.repository.HomeRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.dto.UsersJoinReq;
import com.hana.ddok.users.dto.UsersJoinRes;
import com.hana.ddok.users.exception.UsersInvalidPwd;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final HomeRepository homeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersJoinRes usersJoin(UsersJoinReq req) {
        if(!req.password().equals(req.confirmPassword()))
            throw new UsersInvalidPwd();

        String encodedPwd = bCryptPasswordEncoder.encode(req.password());
        Home home = homeRepository.findById(0L).get();

        Users user = usersRepository.save(UsersJoinReq.toEntity(req, encodedPwd, home));

        return new UsersJoinRes(user.getUsersId(), user.getPhoneNumber());
    }
}
