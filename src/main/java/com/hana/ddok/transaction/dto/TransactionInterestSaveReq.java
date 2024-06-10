package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.spend.domain.SpendType;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;

public record TransactionInterestSaveReq(
        Long amount,
        String recipientTitle,
        String recipientAccount
) {
    public Transaction toEntity(Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.INTEREST)
                .recipientTitle(recipientTitle)
                .recipientAccount(account)
                .build();
    }
}
