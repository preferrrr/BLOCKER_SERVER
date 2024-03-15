package com.blocker.blocker_server.signature.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.signature.exception.SignatureExceptionCode.ALREADY_HAVE_SIGNATURE;

public class AlreadyHaveSignatureException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public AlreadyHaveSignatureException() {
        super(ALREADY_HAVE_SIGNATURE.getMessage());
        this.exceptionCode = ALREADY_HAVE_SIGNATURE;
    }
}
