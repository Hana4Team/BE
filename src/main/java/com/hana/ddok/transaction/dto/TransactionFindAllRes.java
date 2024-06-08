package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;

import java.util.List;

public record TransactionFindAllRes(
        String name,
        String accountNumber,
        Long balance,
        List<TransactionFindByIdRes> transactionList
) {
    public TransactionFindAllRes(Account account, List<TransactionFindByIdRes> transactionFindByIdResList) {
        this(
                account.getProducts().getName(),
                account.getAccountNumber(),
                account.getBalance(),
                transactionFindByIdResList
        );
    }
}