package com.hana.ddok.moneybox.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.moneybox.domain.Moneybox;

public record MoneyboxSaveRes(
        Long accountId,
        Long moneyboxId
) {
    public MoneyboxSaveRes(Account account, Moneybox moneybox) {
        this(
                account.getAccountId(),
                moneybox.getMoneyboxId()
        );
    }
}
