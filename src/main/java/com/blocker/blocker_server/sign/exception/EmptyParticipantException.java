package com.blocker.blocker_server.sign.exception;

import lombok.Getter;

@Getter
public class EmptyParticipantException extends RuntimeException {
    private final String NAME;

    public EmptyParticipantException() {
        NAME = "EmptyParticipantException";
    }
}
