package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;

public record AccountDepositSaveRes(
        Long depositSavingId,
        Long accountId
) {
    public AccountDepositSaveRes(Depositsaving depositsaving, Account account) {
        this(
                depositsaving.getDepositsavingId(),
                account.getAccountId()
        );
    }
}
