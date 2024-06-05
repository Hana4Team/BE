package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;

public record AccountDepositsavingSaveRes(
        Long depositSavingId,
        Long accountId
) {
    public AccountDepositsavingSaveRes(Depositsaving depositsaving, Account account) {
        this(
                depositsaving.getDepositsavingId(),
                account.getAccountId()
        );
    }
}
