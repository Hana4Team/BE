package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record AccountFindAllRes(
    Long accountId,
    Integer type,
    Long balance
) {
    public AccountFindAllRes(Account account) {
        this(
                account.getAccountId(),
                account.getType(),
                account.getBalance()
        );
    }
}
