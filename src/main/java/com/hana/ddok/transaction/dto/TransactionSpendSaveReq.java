package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;

public record TransactionSpendSaveReq(
        Long amount,
        String senderTitle,
        String senderAccount,
        Integer spendType
) {
    public Transaction toEntity(Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(2)
                .senderTitle(senderTitle)
                .senderAccount(account)
                .build();
    }
}
