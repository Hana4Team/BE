package com.hana.ddok.account.dto;

public record AccountFindAllReq(
        Boolean depositAccount,
        Boolean savingsAccount,
        Boolean depositWithdrawalAccount,
        Boolean moneyboxAccount
) {

}
