package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;

public record TransactionMoneyboxSaveReq(
        Integer amount,
        String senderTitle,
        String recipientTitle,
        String senderMoneybox,
        String recipientMoneybox
) {
    public Transaction toEntity(Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.MONEYBOX)
                .senderTitle(senderTitle)
                .recipientTitle(recipientTitle)
                .senderAccount(account)
                .recipientAccount(account)
                .build();
    }
}
