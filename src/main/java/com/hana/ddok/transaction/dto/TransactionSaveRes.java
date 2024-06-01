package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

public record TransactionRes(
        Long transactionId
) {
    public TransactionRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
