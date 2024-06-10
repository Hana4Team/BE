package com.hana.ddok.transaction.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class TransactionAccessDenied extends AccessDeniedException {
    public TransactionAccessDenied() {
        super("이 Users의 거래 내역을 확인할 수 없습니다.");
    }
}
