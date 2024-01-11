package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class IsNotContractParticipantException extends RuntimeException {
    private final String NAME;

    public IsNotContractParticipantException(String message) {
        super(message);
        NAME = "IsNotParticipantException";
    }
}
