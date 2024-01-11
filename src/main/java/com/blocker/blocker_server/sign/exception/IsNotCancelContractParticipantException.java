package com.blocker.blocker_server.sign.exception;

import lombok.Getter;

@Getter
public class IsNotCancelContractParticipantException extends RuntimeException {
    private final String NAME;

    public IsNotCancelContractParticipantException(String message) {
        super(message);
        NAME = "IsNotCancelContractParticipantException";
    }
}
