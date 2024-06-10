package com.hana.ddok.users.exception;

import com.hana.ddok.common.exception.AccessDeniedException;

public class UsersReadNewsUpdateDenied extends AccessDeniedException {

    public UsersReadNewsUpdateDenied() {
        super("이미 뉴스를 읽은 회원입니다.");
    }
}
