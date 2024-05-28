package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record AccountSaveRes(
        Long accountId
) {
    public AccountSaveRes(Account account) {
        this(
                account.getAccountId()
        );
    }
}
