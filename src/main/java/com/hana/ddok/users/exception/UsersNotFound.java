package com.hana.ddok.users.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class UsersNotFound extends EntityNotFoundException {

    public UsersNotFound() {
        super("해당 유저를 찾을 수 없습니다.");
    }
}
