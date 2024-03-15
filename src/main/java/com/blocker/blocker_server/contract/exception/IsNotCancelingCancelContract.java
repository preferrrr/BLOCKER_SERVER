package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.IS_NOT_CANCELING_CANCEL_CONTRACT;

public class IsNotCancelingCancelContract extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotCancelingCancelContract() {
        super(IS_NOT_CANCELING_CANCEL_CONTRACT.getMessage());
        this.exceptionCode = IS_NOT_CANCELING_CANCEL_CONTRACT;
    }
}
