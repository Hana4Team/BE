package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

public record TransactionWithdrawalSaveRes(
        Long transactionId
) {
    public TransactionWithdrawalSaveRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
