package com.hana.ddok.depositsaving.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import java.time.LocalDate;

public record DepositsavingSaveReq(
        Integer payment,
        LocalDate endDate,
        Long productsId,
        Long withdrawalAccountId
) {
    public Depositsaving toEntity(Account account, Account withdrawalAccount) {
        return Depositsaving.builder()
                .payment(payment)
                .endDate(endDate)
                .withdrawalAccount(withdrawalAccount)
                .account(account)
                .build();
    }

    public Account toAccount(Users users, Products products, String accountNumber, String password) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(0L)
                .interest(products.getInterest2())
                .password(password)
                .isDeleted(false)
                .isMissionConnected(false)
                .users(users)
                .products(products)
                .build();
    }
}
