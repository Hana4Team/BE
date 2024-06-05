package com.hana.ddok.users.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.home.domain.Home;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;


import java.time.LocalDate;


public record UsersJoinReq(String name, LocalDate birthDate, String phoneNumber, String password, String confirmPassword) {
    public static Users toEntity(UsersJoinReq req, String encodedPwd, Home home) {
        return Users.builder()
                .name(req.name)
                .birthDate(req.birthDate)
                .phoneNumber(req.phoneNumber)
                .password(encodedPwd)
                .step(0)
                .stepStatus(null)
                .points(0)
                .readNews(false)
                .home(home)
                .build();
    }

    public Account toAccount(Users users, Products products, String accountNumber, String password) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(10000000L)
                .interest(products.getInterest2())
                .password(password)
                .isDeleted(false)
                .users(users)
                .products(products)
                .build();
    }
}
