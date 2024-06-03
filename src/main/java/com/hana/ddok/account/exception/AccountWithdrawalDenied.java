package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class AccountWithdrawalDenied extends AccessDeniedException {
    public AccountWithdrawalDenied() {
        super("해당 계좌에서 출금할 수 없습니다.");
    }
}
