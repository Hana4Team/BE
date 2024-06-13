package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class AccountSaveDenied extends AccessDeniedException {
    public AccountSaveDenied() {
        super("이미 가입한 상품이므로 계좌를 개설할 수 없습니다.");
    }
}
