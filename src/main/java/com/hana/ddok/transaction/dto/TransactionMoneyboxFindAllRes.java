package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;

import java.util.List;

public record TransactionMoneyboxFindAllRes(
        String name,
        String accountNumber,
        Long balance,
        List<TransactionMoneyboxFindByIdRes> transactionList
) {
    public TransactionMoneyboxFindAllRes(Account account, List<TransactionMoneyboxFindByIdRes> transactionMoneyboxFindByIdResList) {
        this(
                account.getProducts().getName(),
                account.getAccountNumber(),
                account.getBalance(),
                transactionMoneyboxFindByIdResList
        );
    }
}