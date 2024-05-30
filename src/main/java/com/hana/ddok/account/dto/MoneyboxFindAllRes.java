package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record MoneyboxFindAllRes(
        Long parkingBalance,
        Long expenseBalance,
        Long savingBalance
) {
    public MoneyboxFindAllRes(Account parkingAccount, Account expenseAccount, Account savingAccount) {
        this(
                parkingAccount.getBalance(),
                expenseAccount.getBalance(),
                savingAccount.getBalance()
        );
    }
}
