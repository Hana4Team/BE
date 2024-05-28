package com.hana.ddok.users.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class UsersInvalidPwd extends ValueInvalidException {
    public UsersInvalidPwd() {
        super("비밀번호 재입력이 같지 않습니다");
    }
}
