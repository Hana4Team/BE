package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record AccountFindAllRes(
    Long accountId,
    String name,
    Long balance
) {
    public AccountFindAllRes(Account account) {
        this(
                account.getAccountId(),
                account.getProducts().getName(),
                account.getBalance()
        );
    }

    public AccountFindAllRes(Account account, Long moneyboxTotalBalance) {
        this(
                account.getAccountId(),
                account.getProducts().getName(),
                moneyboxTotalBalance
        );
    }
}
