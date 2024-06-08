package com.hana.ddok.account.dto;

public record AccountDeleteReq(
        Long withdrawalAccountId,
        Long depositAccountId
) {
}
