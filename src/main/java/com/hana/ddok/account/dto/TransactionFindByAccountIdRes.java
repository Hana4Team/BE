package com.hana.ddok.account.dto;

import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDateTime;

public record TransactionFindByAccountIdRes(
        Boolean isSender,
        String title,
        Long amount,
        LocalDateTime createdAt
) {
    public TransactionFindByAccountIdRes(Transaction transaction, Boolean isSender) {
        this(
                isSender,
                isSender ? transaction.getSenderTitle() : transaction.getRecipientTitle(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
    }
}
