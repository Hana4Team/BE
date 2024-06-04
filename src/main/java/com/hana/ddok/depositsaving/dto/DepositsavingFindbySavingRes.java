package com.hana.ddok.depositsaving.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDate;

public record DepositsavingFindbySavingRes(
        Long accountId,
        String ProductName,
        Long balance,
        Float interest,
        Long initialAmount,
        Integer payment,
        LocalDate startDate,
        LocalDate endDate,
        Long targetAmount
        ) {
    public DepositsavingFindbySavingRes(Account account, Depositsaving depositsaving, Transaction transaction) {
        this(
                account.getAccountId(),
                account.getProducts().getName(),
                account.getBalance(),
                account.getInterest(),
                transaction.getAmount(),
                depositsaving.getPayment(),
                account.getCreatedAt().toLocalDate(),
                depositsaving.getEndDate(),
                depositsaving.getPayment().longValue() * 100
        );
    }
}
