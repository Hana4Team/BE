package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.products.domain.ProductsType;

public record AccountFindAllRes(
    Long accountId,
    String name,
    Long balance,
    String accountNumber,
    ProductsType type
) {
    public AccountFindAllRes(Account account) {
        this(
                account.getAccountId(),
                account.getProducts().getName(),
                account.getBalance(),
                account.getAccountNumber(),
                account.getProducts().getType()
        );
    }
}
