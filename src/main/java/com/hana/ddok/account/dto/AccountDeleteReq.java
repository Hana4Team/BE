package com.hana.ddok.account.dto;

public record AccountDeleteReq(
        Long deleteAccountId,
        Long depositAccountId
) {
}
