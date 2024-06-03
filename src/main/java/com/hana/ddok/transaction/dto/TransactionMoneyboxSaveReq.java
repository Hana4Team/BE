package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;

public record TransactionMoneyboxSaveReq(
        Long amount,
        String senderTitle,
        String recipientTitle,
        String senderMoneybox,
        String recipientMoneybox
) {
    public Transaction toEntity(Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(4)
                .senderTitle(senderTitle)
                .recipientTitle(recipientTitle)
                .senderAccount(account)
                .recipientAccount(account)
                .build();
    }
}
