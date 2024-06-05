package com.hana.ddok.depositsaving.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDate;

public record DepositsavingFindbyTypeRes(
        Long accountId,
        String ProductName,
        Long balance,
        Float interest,
        LocalDate startDate,
        LocalDate endDate,
        Long initialAmount,
        Integer payment,
        Long targetAmount
        ) {
    public DepositsavingFindbyTypeRes(Account account, Depositsaving depositsaving, Long initialAmount, Integer payment, Long targetAmount) {
        this(
                account.getAccountId(),
                account.getProducts().getName(),
                account.getBalance(),
                account.getInterest(),
                account.getCreatedAt().toLocalDate(),
                depositsaving.getEndDate(),
                initialAmount,
                payment,
                targetAmount
        );
    }
}
