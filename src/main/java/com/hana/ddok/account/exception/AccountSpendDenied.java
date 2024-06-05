package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class AccountSpendDenied extends AccessDeniedException {
    public AccountSpendDenied() {
        super("해당 계좌에서 지출할 수 없습니다.");
    }
}
