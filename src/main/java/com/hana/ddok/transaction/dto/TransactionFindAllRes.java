package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDate;

public record TransactionFindAllRes(
        Boolean isSender,
        String title,
        Long amount,
        LocalDate date
) {
    public TransactionFindAllRes(Transaction transaction, Boolean isSender) {
        this(
                isSender,
                isSender ? transaction.getSenderTitle() : transaction.getRecipientTitle(),
                transaction.getAmount().longValue(),
                transaction.getCreatedAt().toLocalDate()
        );
    }
}