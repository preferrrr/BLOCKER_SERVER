package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.IS_NOT_CANCEL_CONTRACT_PARTICIPANT;

public class IsNotContractParticipantException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotContractParticipantException() {
        super(IS_NOT_CANCEL_CONTRACT_PARTICIPANT.getMessage());
        this.exceptionCode = IS_NOT_CANCEL_CONTRACT_PARTICIPANT;
    }
}
