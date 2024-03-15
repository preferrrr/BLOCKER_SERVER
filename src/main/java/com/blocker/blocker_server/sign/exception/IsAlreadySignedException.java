package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.sign.exception.SignExceptionCode.IS_ALREADY_SIGNED;

public class IsAlreadySignedException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsAlreadySignedException() {
        super(IS_ALREADY_SIGNED.getMessage());
        this.exceptionCode = IS_ALREADY_SIGNED;
    }
}
