package com.hana.ddok.home.service;

import com.hana.ddok.home.dto.HomeFindByUsersRes;
import com.hana.ddok.home.exception.HomeNotFound;
import com.hana.ddok.home.repository.HomeRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final HomeRepository homeRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public HomeFindByUsersRes homeFindByUsers(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        HomeFindByUsersRes homeFindByUsersRes = homeRepository.findById(users.getHome().getHomeId())
                .map(HomeFindByUsersRes::new)
                .orElseThrow(() -> new HomeNotFound());
        return homeFindByUsersRes;
    }
}
