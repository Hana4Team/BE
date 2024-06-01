package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record AccountFindByIdRes(
    String name,
    String accountNumber,
    Long balance
) {
    public AccountFindByIdRes(Account account) {
        this(
                account.getProducts().getName(),
                account.getAccountNumber(),
                account.getBalance()
        );
    }
}
