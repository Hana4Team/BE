package com.hana.ddok.account.dto;

public record AccountPasswordCheckReq(
        String accountNumber,
        String password
) {

}
