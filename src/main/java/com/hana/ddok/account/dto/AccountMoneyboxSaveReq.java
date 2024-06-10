package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

public record AccountMoneyboxSaveReq(
        String password,
        Long productsId
) {
    public Account toEntity(Users users, Products products, String accountNumber) {
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

    public Moneybox toMoneybox(Account account) {
        return Moneybox.builder()
                .parkingBalance(0L)
                .expenseBalance(0L)
                .savingBalance(0L)
                .isCharged(false)
                .account(account)
                .build();
    }
}
