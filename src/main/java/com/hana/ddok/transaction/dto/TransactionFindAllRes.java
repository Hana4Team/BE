package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;

import java.util.List;
import java.util.stream.Collectors;

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