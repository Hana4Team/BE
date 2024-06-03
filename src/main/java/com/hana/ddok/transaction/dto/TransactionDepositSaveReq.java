package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;

public record TransactionDepositSaveReq(
        Long amount,
        String recipientTitle,
        String recipientAccount
) {
    public Transaction toEntity(Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(2)
                .recipientTitle(recipientTitle)
                .recipientAccount(account)
                .build();
    }
}
