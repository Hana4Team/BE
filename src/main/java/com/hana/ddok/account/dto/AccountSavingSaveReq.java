package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import java.time.LocalDate;

public record AccountSavingSaveReq(
        Long initialAmount,
        Integer payment,
        Integer payDate,
        LocalDate endDate,
        Long productsId,
        Long withdrawalAccountId
) {
    public Account toEntity(Users users, Products products, String accountNumber, String password) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(0L)
                .interest(products.getInterest1())
                .password(password)
                .isDeleted(false)
                .users(users)
                .products(products)
                .build();
    }

    public Depositsaving toDepositsaving(Account account, Account withdrawalAccount) {
        return Depositsaving.builder()
                .payment(payment)
                .payDate(payDate)
                .endDate(endDate)
                .withdrawalAccount(withdrawalAccount)
                .account(account)
                .build();
    }
}
