package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class AccountDepositDenied extends AccessDeniedException {
    public AccountDepositDenied() {
        super("해당 계좌에 입금할 수 없습니다.");
    }
}
