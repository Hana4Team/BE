package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;

public record TransactionSaveReq(
        Integer amount,
        String senderTitle,
        String recipientTitle,
        String senderAccount,
        String recipientAccount
) {
    public Transaction toEntity(Account senderAccount, Account recipientAccount) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.REMITTANCE)
                .senderTitle(senderTitle)
                .recipientTitle(recipientTitle)
                .senderAccount(senderAccount)
                .recipientAccount(recipientAccount)
                .build();
    }
}
