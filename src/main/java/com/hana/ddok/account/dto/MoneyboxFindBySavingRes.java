package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record MoneyboxFindBySavingRes(
        Long savingBalance
) {
    public MoneyboxFindBySavingRes(Account account) {
        this(
                account.getBalance()
        );
    }
}
