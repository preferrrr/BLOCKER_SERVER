package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.sign.exception.SignExceptionCode.IS_NOT_CONCLUDE_CONTRACT_FOR_CANCEL;

public class IsNotConcludeContractForCancelException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotConcludeContractForCancelException() {
        super(IS_NOT_CONCLUDE_CONTRACT_FOR_CANCEL.getMessage());
        this.exceptionCode = IS_NOT_CONCLUDE_CONTRACT_FOR_CANCEL;
    }
}
