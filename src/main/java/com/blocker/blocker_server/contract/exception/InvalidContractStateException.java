package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.INVALID_CONTRACT_STATE;

public class InvalidContractStateException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public InvalidContractStateException() {
        super(INVALID_CONTRACT_STATE.getMessage());
        this.exceptionCode = INVALID_CONTRACT_STATE;
    }
}
