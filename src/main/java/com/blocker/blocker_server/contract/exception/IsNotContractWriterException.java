package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.IS_NOT_CONTRACT_WRITER;

public class IsNotContractWriterException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotContractWriterException() {
        super(IS_NOT_CONTRACT_WRITER.getMessage());
        this.exceptionCode = IS_NOT_CONTRACT_WRITER;
    }
}
