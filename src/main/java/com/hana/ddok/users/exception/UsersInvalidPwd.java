package com.hana.ddok.users.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class UsersInvalidPwd extends ValueInvalidException {
    public UsersInvalidPwd() {
        super("유효하지 않은 비밀번호 입력입니다.");
    }
}
