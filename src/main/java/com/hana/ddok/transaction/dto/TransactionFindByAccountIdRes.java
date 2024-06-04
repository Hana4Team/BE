package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDate;

public record TransactionFindByAccountIdRes(
        Boolean isSender,
        String title,
        Long amount,
        LocalDate date
) {
    public TransactionFindByAccountIdRes(Transaction transaction, Boolean isSender) {
        this(
                isSender,
                isSender ? transaction.getSenderTitle() : transaction.getRecipientTitle(),
                transaction.getAmount(),
                transaction.getCreatedAt().toLocalDate()
        );
    }
}
