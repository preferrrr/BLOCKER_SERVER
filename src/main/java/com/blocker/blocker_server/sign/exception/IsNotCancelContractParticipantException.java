package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.sign.exception.SignExceptionCode.IS_NOT_CANCEL_CONTRACT_PARTICIPANT;

public class IsNotCancelContractParticipantException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotCancelContractParticipantException() {
        super(IS_NOT_CANCEL_CONTRACT_PARTICIPANT.getMessage());
        this.exceptionCode = IS_NOT_CANCEL_CONTRACT_PARTICIPANT;
    }
}
