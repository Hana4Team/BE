package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

public record AccountChargeRes(
        Long transactionId
) {
    public AccountChargeRes(Transaction transaction) {
        this(
                transaction.getTransactionId()
        );
    }
}
