package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.util.AccountNumberGenerator;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import java.time.LocalDate;

public record AccountSaveReq(
        Integer payment,
        LocalDate endDate,
        String password,
        Long productsId
) {
    public Account toEntity(Users users, Products products, String accountNumber) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(0L)
                .payment(payment)
                .endDate(endDate)
                .password(password)
                .isDeleted(0)
                .users(users)
                .products(products)
                .build();
    }
}
