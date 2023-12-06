package com.blocker.blocker_server.commons.exception;

import lombok.Getter;

@Getter
public class FailSaveSignatureException extends RuntimeException{
    private final String NAME;

    public FailSaveSignatureException(String message) {
        super(message);
        NAME = "FailSaveSignatureException";
    }
}
