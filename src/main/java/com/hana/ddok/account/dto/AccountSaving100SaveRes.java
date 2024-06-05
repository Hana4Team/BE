package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.moneybox.domain.Moneybox;

public record AccountSaving100SaveRes(
        Long depositSavingId,
        Long accountId
) {
    public AccountSaving100SaveRes(Depositsaving depositsaving, Account account) {
        this(
                depositsaving.getDepositsavingId(),
                account.getAccountId()
        );
    }
}
