package com.hana.ddok.moneybox.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.util.AccountNumberGenerator;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import java.time.LocalDate;

public record MoneyboxSaveReq(
        String password,
        Long productsId
) {

    public Moneybox toEntity(Account account) {
        return Moneybox.builder()
                .parkingBalance(0L)
                .expenseBalance(0L)
                .savingBalance(0L)
                .expenseTotal(0L)
                .isCharged(false)
                .account(account)
                .build();
    }

    public Account toAccount(Users users, Products products, String accountNumber) {
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
