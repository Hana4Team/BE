package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import java.time.LocalDate;

public record AccountDepositsavingDeleteReq(
        Long withdrawalAccountId,
        Long depositAccountId
) {
}