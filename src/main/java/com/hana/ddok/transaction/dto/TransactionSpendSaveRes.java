package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

public record TransactionSpendSaveRes(
        Long transactionId
) {
    public TransactionSpendSaveRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
