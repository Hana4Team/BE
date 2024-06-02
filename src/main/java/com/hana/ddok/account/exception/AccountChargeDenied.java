package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class AccountChargeDenied extends AccessDeniedException {
    public AccountChargeDenied() {
        super("해당 계좌에 충전할 수 없습니다.");
    }
}
