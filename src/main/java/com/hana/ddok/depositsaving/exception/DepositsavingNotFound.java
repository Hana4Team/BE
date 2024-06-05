package com.hana.ddok.depositsaving.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class DepositsavingNotFound extends EntityNotFoundException {
    public DepositsavingNotFound() {
        super("예적금을 찾을 수 없습니다.");
    }
}
