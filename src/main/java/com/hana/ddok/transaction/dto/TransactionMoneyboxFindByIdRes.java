package com.hana.ddok.transaction.dto;

import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDateTime;

public record TransactionMoneyboxFindByIdRes(
        Boolean isSender,
        String title,
        Long amount,
        LocalDateTime dateTime
) {
    public TransactionMoneyboxFindByIdRes(Transaction transaction, Boolean isSender) {
        this(
                isSender,
                isSender ? transaction.getSenderTitle() : transaction.getRecipientTitle(),
                transaction.getAmount().longValue(),
                transaction.getCreatedAt()
        );
    }
}