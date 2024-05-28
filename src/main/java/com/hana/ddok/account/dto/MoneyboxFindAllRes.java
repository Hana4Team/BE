package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record MoneyboxFindAllRes(
        Long parking,
        Long expense,
        Long saving
) {
    public MoneyboxFindAllRes(Account parkingAccount, Account expenseAccount, Account savingAccount) {
        this(
                parkingAccount.getBalance(),
                expenseAccount.getBalance(),
                savingAccount.getBalance()
        );
    }
}
