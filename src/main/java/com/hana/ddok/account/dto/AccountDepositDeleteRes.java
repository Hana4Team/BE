package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record AccountDepositDeleteRes(
        Long accountId
) {
    public AccountDepositDeleteRes(Account account) {
        this(
                account.getAccountId()
        );
    }
}
