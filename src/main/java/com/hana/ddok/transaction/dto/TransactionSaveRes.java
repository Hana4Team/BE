package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

public record TransactionSaveRes(
        Long transactionId
) {
    public TransactionSaveRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
