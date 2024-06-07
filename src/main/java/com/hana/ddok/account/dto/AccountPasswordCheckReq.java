package com.hana.ddok.account.dto;

public record AccountPasswordCheckReq(
        Long accountId,
        String password
) {

}
