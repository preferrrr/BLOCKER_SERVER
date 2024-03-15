package com.blocker.blocker_server.sign.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.sign.exception.SignExceptionCode.EMPTY_PARTICIPANT;

public class EmptyParticipantException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public EmptyParticipantException() {
        super(EMPTY_PARTICIPANT.getMessage());
        this.exceptionCode = EMPTY_PARTICIPANT;
    }
}
