package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;

public record AccountDepositsavingDeleteRes(
        Long depositSavingId,
        Long accountId
) {
    public AccountDepositsavingDeleteRes(Depositsaving depositsaving, Account account) {
        this(
                depositsaving.getDepositsavingId(),
                account.getAccountId()
        );
    }
}
