package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

public record TransactionMoneyboxSaveRes(
        Long transactionId
) {
    public TransactionMoneyboxSaveRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
