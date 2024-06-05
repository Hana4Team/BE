package com.hana.ddok.transaction.dto;

import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.transaction.domain.Transaction;

public record TransactionInterestSaveRes(
        Long transactionId
) {
    public TransactionInterestSaveRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
