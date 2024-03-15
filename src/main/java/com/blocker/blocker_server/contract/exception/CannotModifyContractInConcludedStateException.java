package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.CANNOT_MODIFY_CONTRACT_IN_CONCLUDED;

public class CannotModifyContractInConcludedStateException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public CannotModifyContractInConcludedStateException() {
        super(CANNOT_MODIFY_CONTRACT_IN_CONCLUDED.getMessage());
        this.exceptionCode = CANNOT_MODIFY_CONTRACT_IN_CONCLUDED;
    }
}
