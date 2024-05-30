package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record MoneyboxSaveRes(
        Long parkingAccountId,
        Long expenseAccountId,
        Long savingAccountId
) {
    public MoneyboxSaveRes(Account parkingAccount, Account expenseAccount, Account savingAccount) {
        this(
                parkingAccount.getAccountId(),
                expenseAccount.getAccountId(),
                savingAccount.getAccountId()
        );
    }
}
