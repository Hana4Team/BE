package com.hana.ddok.common.aop;

import com.hana.ddok.common.dto.ErrorResponse;
import com.hana.ddok.common.exception.AccessDeniedException;
import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.common.exception.ValueInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * [EntityNotFoundException] 발생 시 404 NOT FOUND 응답
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(
            EntityNotFoundException exception) {

        return new ResponseEntity<>(new ErrorResponse(exception), HttpStatus.NOT_FOUND);
    }

    /**
     * [ValueInvalidException] 발생 시 400 BAD_REQUEST 응답
     */
    @ExceptionHandler(ValueInvalidException.class)
    public ResponseEntity<ErrorResponse> valueInvalidExceptionHandler(
        ValueInvalidException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception), HttpStatus.BAD_REQUEST);
    }

    /**
     * [MatchRoomFullException] 발생 시 403 FORBIDDEN 응답
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> matchRoomFullExceptionHandler(
        AccessDeniedException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception), HttpStatus.FORBIDDEN);
    }
}

