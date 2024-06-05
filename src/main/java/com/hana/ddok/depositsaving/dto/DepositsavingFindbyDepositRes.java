package com.hana.ddok.depositsaving.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDate;

public record DepositsavingFindbyDepositRes(
        Long accountId,
        String ProductName,
        Long balance,
        Float interest,
        Long initialAmount,
        LocalDate startDate,
        LocalDate endDate
        ) {
    public DepositsavingFindbyDepositRes(Account account, Depositsaving depositsaving, Transaction transaction) {
        this(
                account.getAccountId(),
                account.getProducts().getName(),
                account.getBalance(),
                account.getInterest(),
                transaction.getAmount(),
                account.getCreatedAt().toLocalDate(),
                depositsaving.getEndDate()
        );
    }
}
