package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

public record TransactionDepositSaveRes(
        Long transactionId
) {
    public TransactionDepositSaveRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
