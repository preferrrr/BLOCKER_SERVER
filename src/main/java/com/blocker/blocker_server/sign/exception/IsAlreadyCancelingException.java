package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.sign.exception.SignExceptionCode.IS_ALREADY_CANCELING;

public class IsAlreadyCancelingException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsAlreadyCancelingException() {
        super(IS_ALREADY_CANCELING.getMessage());
        this.exceptionCode = IS_ALREADY_CANCELING;
    }
}
