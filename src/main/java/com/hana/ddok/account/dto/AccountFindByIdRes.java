package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.dto.TransactionFindByAccountIdRes;

import java.util.List;
import java.util.stream.Collectors;

public record AccountFindByIdRes(
    String name,
    String accountNumber,
    Long balance,
    List<TransactionFindByAccountIdRes> transactionList
) {
    public AccountFindByIdRes(Account account, List<Transaction> transactionList) {
        this(
                account.getProducts().getName(),
                account.getAccountNumber(),
                account.getBalance(),
                transactionList.stream()
                        .map(transaction -> new TransactionFindByAccountIdRes(transaction, account.getAccountId() == transaction.getSenderAccount().getAccountId()))
                        .collect(Collectors.toList())
        );
    }
}
