package com.hana.ddok.home.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class HomeNotFound extends EntityNotFoundException {
    public HomeNotFound() {
        super("집을 찾을 수 없습니다.");
    }
}
