package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.spend.domain.SpendType;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;

public record TransactionSpendSaveReq(
        Long amount,
        String senderTitle,
        ProductsType senderAccountType,
        SpendType spendType
) {
    public Transaction toEntity(Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.SPEND)
                .senderTitle(senderTitle)
                .senderAccount(account)
                .build();
    }

    public Spend toSpend(Transaction transaction) {
        return Spend.builder()
                .amount(amount)
                .type(spendType)
                .transaction(transaction)
                .build();
    }
}
