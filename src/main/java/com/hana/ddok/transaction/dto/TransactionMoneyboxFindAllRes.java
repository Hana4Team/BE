package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDateTime;

public record TransactionMoneyboxFindAllRes(
        Boolean isSender,
        String title,
        Long amount,
        LocalDateTime dateTime
) {
    public TransactionMoneyboxFindAllRes(Transaction transaction, Boolean isSender) {
        this(
                isSender,
                isSender ? transaction.getSenderTitle() : transaction.getRecipientTitle(),
                transaction.getAmount().longValue(),
                transaction.getCreatedAt()
        );
    }
}