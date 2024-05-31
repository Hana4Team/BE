package com.hana.ddok.common.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    Boolean success;
    String type;
    String message;

    public ErrorResponse(Throwable e) {
        this.success = false;
        this.type = e.getClass().getSimpleName();
        this.message = e.getMessage();
    }
}
