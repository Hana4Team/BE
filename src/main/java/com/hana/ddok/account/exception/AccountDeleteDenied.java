package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class AccountDeleteDenied extends AccessDeniedException {
    public AccountDeleteDenied() {
        super("해당 상품의 계좌는 해지할 수 없습니다.");
    }
}
