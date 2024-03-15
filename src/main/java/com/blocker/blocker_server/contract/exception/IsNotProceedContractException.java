package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.IS_NOT_NOTPROCEED_CONTRACT;
import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.IS_NOT_PROCEED_CONTRACT;

public class IsNotProceedContractException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotProceedContractException() {
        super(IS_NOT_PROCEED_CONTRACT.getMessage());
        this.exceptionCode = IS_NOT_PROCEED_CONTRACT;
    }
}
