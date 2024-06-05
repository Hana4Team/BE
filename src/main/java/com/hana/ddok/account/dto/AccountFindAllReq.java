package com.hana.ddok.account.dto;

public record AccountFindAllReq(
        Boolean depositWithdrawalAccount,
        Boolean depositAccount,
        Boolean saving100Account,
        Boolean savingAccount,
        Boolean moneyboxAccount
) {

}
