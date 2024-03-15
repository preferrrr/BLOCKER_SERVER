package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.CANCEL_CONTRACT_NOT_FOUND;

public class CancelContractNotFoundException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public CancelContractNotFoundException() {
        super(CANCEL_CONTRACT_NOT_FOUND.getMessage());
        this.exceptionCode = CANCEL_CONTRACT_NOT_FOUND;
    }
}
