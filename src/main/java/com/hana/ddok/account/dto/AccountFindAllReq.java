package com.hana.ddok.account.dto;

public record AccountFindAllReq(
        Boolean depositWithdrawalAccount,
        Boolean depositAccount,
        Boolean savingsAccount,
        Boolean moneyboxAccount
) {

}
