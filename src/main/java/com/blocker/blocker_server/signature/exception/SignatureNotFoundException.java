package com.blocker.blocker_server.signature.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.signature.exception.SignatureExceptionCode.SIGNATURE_NOT_FOUND;

public class SignatureNotFoundException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public SignatureNotFoundException() {
        super(SIGNATURE_NOT_FOUND.getMessage());
        this.exceptionCode = SIGNATURE_NOT_FOUND;
    }
}
