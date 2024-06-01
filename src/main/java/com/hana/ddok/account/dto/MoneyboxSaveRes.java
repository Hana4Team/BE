package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record MoneyboxSaveRes(
        Long accountId
) {
    public MoneyboxSaveRes(Account account) {
        this(
                account.getAccountId()
        );
    }
}
