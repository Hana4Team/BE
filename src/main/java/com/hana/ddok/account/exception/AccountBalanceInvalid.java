package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class AccountBalanceInvalid extends ValueInvalidException {
    public AccountBalanceInvalid() {
        super("계좌 잔액이 없습니다.");
    }
}
