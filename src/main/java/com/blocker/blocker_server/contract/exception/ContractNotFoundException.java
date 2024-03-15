package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.CANNOT_MODIFY_CONTRACT_IN_CONCLUDED;
import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.CONTRACT_NOT_FOUND;

public class ContractNotFoundException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public ContractNotFoundException() {
        super(CONTRACT_NOT_FOUND.getMessage());
        this.exceptionCode = CONTRACT_NOT_FOUND;
    }
}
