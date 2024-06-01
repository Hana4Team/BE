package com.hana.ddok.account.dto;


import com.hana.ddok.account.domain.Moneybox;

public record MoneyboxFindBySavingRes(
        Long savingBalance
) {
    public MoneyboxFindBySavingRes(Moneybox moneybox) {
        this(
                moneybox.getSavingBalance()
        );
    }
}
