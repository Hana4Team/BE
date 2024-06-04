package com.hana.ddok.transaction.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class TransactionNotFound extends EntityNotFoundException {
    public TransactionNotFound() {
        super("거래를 찾을 수 없습니다.");
    }
}
