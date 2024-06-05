package com.hana.ddok.users.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class UsersExistPhoneNumber extends ValueInvalidException {
    public UsersExistPhoneNumber() {
        super("이미 존재하는 휴대폰번호입니다.");
    }
}
