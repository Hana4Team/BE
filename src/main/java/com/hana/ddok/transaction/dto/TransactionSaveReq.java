package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;

public record TransactionSaveReq(
        Long amount,
        Integer type,
        String senderTitle,
        String recipientTitle,
        String senderAccount,
        String recipientAccount
) {
    public Transaction toEntity(Account senderAccount, Account recipientAccount) {
        return Transaction.builder()
                .amount(amount)
                .type(type)
                .senderTitle(senderTitle)
                .recipientTitle(recipientTitle)
                .senderAccount(senderAccount)
                .recipientAccount(recipientAccount)
                .build();
    }
}
