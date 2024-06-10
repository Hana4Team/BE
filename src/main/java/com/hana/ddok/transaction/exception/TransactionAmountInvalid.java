package com.hana.ddok.transaction.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class TransactionAmountInvalid extends ValueInvalidException {
    public TransactionAmountInvalid() {
        super("양수의 값만 거래할 수 있습니다.");
    }
}
