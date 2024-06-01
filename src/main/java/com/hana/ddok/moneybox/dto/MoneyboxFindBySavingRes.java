package com.hana.ddok.moneybox.dto;


import com.hana.ddok.moneybox.domain.Moneybox;

public record MoneyboxFindBySavingRes(
        Long savingBalance
) {
    public MoneyboxFindBySavingRes(Moneybox moneybox) {
        this(
                moneybox.getSavingBalance()
        );
    }
}
