package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;

public record TransactionSaveReq(
        Long amount,
        String senderTitle,
        String recipientTitle,
        String senderAccount,
        String recipientAccount
) {
    public Transaction toEntity(Account senderAccount, Account recipientAccount) {
        return Transaction.builder()
                .amount(amount)
                .type(1)
                .senderTitle(senderTitle)
                .recipientTitle(recipientTitle)
                .senderAccount(senderAccount)
                .recipientAccount(recipientAccount)
                .build();
    }
}
