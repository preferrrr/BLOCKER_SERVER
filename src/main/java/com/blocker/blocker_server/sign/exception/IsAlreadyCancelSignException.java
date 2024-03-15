package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.sign.exception.SignExceptionCode.IS_ALREADY_CANCEL_SIGN;

public class IsAlreadyCancelSignException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsAlreadyCancelSignException() {
        super(IS_ALREADY_CANCEL_SIGN.getMessage());
        this.exceptionCode = IS_ALREADY_CANCEL_SIGN;
    }
}
