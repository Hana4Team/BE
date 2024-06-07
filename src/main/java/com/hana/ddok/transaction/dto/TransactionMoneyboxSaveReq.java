package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.moneybox.domain.MoneyboxType;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;

public record TransactionMoneyboxSaveReq(
        Integer amount,
        MoneyboxType senderMoneybox,
        MoneyboxType recipientMoneybox
) {
    public Transaction toEntity(Account account, String title) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.MONEYBOX)
                .senderTitle(title)
                .recipientTitle(title)
                .senderAccount(account)
                .recipientAccount(account)
                .build();
    }
}
