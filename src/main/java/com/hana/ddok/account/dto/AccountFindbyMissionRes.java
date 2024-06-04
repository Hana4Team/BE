package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDate;

public record AccountFindbyMissionRes(
        Long accountId,
        String ProductName,
        Long balance,
        Float interest,
        LocalDate startDate,
        LocalDate endDate,
        Long initialAmount,
        Long targetAmount
        ) {
    public AccountFindbyMissionRes(Account account, Depositsaving depositsaving, Transaction transaction, Integer monthPeriod) {
        this(
                account.getAccountId(),
                account.getProducts().getName(),
                account.getBalance(),
                account.getInterest(),
                account.getCreatedAt().toLocalDate(),
                depositsaving.getEndDate(),
                transaction.getAmount(),
                depositsaving.getPayment().longValue() * monthPeriod
        );
    }
}
