package com.hana.ddok.account.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class MoneyboxNotFound extends EntityNotFoundException {
    public MoneyboxNotFound() {
        super("머니박스를 찾을 수 없습니다.");
    }
}
