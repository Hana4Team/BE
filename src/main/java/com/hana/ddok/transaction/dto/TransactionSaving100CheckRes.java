package com.hana.ddok.transaction.dto;

import com.hana.ddok.account.domain.Account;

import java.util.List;

public record TransactionSaving100CheckRes(
        Integer successCount,
        Integer failCount
) {
}