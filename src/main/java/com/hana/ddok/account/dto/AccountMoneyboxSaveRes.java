package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.moneybox.domain.Moneybox;

public record AccountMoneyboxSaveRes(
        Long accountId,
        Long moneyboxId
) {
    public AccountMoneyboxSaveRes(Account account, Moneybox moneybox) {
        this(
                account.getAccountId(),
                moneybox.getMoneyboxId()
        );
    }
}
