package com.hana.ddok.users.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class UsersPointsInvalid extends ValueInvalidException {
    public UsersPointsInvalid() {
        super("하나머니는 마이너스가 될 수 없습니다.");
    }
}
