package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;

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
                        .map(transaction -> new TransactionFindByAccountIdRes(
                                account.getAccountId() == transaction.getSenderAccount().getAccountId(),
                                account.getAccountId() == transaction.getSenderAccount().getAccountId()
                                        ? transaction.getSenderTitle()
                                        : transaction.getRecipientTitle(),
                                transaction.getAmount(),
                                transaction.getCreatedAt()))
                        .collect(Collectors.toList())
        );
    }
}
