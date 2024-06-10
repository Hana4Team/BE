package com.hana.ddok.users.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class UsersStepDenied extends AccessDeniedException {
    public UsersStepDenied() {
        super("해당 users의 step에 맞는 API가 아닙니다.");
    }
}
