package com.blocker.blocker_server.sign.exception;

import lombok.Getter;

@Getter
public class IsNotContractParticipantForCancelException extends RuntimeException {
    private final String NAME;

    public IsNotContractParticipantForCancelException(String message) {
        super(message);
        NAME = "IsNotContractParticipantForCancelException";
    }
}
