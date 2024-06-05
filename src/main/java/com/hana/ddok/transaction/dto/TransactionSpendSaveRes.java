package com.hana.ddok.transaction.dto;

import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.transaction.domain.Transaction;

public record TransactionSpendSaveRes(
        Long transactionId,
        Long spendId
) {
    public TransactionSpendSaveRes(Transaction transaction, Spend spend) {
        this(
                transaction.getTransactionId(),
                spend.getSpendId()
        );
    }
}
