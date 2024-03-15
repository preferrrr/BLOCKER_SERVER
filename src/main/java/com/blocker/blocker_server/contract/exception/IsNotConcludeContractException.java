package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.IS_NOT_CONCLUDE_CONTRACT;

public class IsNotConcludeContractException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotConcludeContractException() {
        super(IS_NOT_CONCLUDE_CONTRACT.getMessage());
        this.exceptionCode = IS_NOT_CONCLUDE_CONTRACT;
    }
}
