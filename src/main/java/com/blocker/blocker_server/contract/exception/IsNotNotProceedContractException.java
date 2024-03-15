package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.*;

public class IsNotNotProceedContractException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotNotProceedContractException() {
        super(IS_NOT_NOTPROCEED_CONTRACT.getMessage());
        this.exceptionCode = IS_NOT_NOTPROCEED_CONTRACT;
    }
}
