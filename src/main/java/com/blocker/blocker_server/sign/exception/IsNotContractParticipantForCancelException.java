package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.sign.exception.SignExceptionCode.IS_NOT_CONTRACT_PARTICIPANT_FOR_CANCEL;

public class IsNotContractParticipantForCancelException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotContractParticipantForCancelException() {
        super(IS_NOT_CONTRACT_PARTICIPANT_FOR_CANCEL.getMessage());
        this.exceptionCode = IS_NOT_CONTRACT_PARTICIPANT_FOR_CANCEL;
    }
}
