package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class AccountNotFound extends EntityNotFoundException {
    public AccountNotFound() {
        super("계좌를 찾을 수 없습니다.");
    }
}
