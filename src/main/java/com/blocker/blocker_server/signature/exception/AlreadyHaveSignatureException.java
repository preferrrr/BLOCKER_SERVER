package com.blocker.blocker_server.signature.exception;

import lombok.Getter;

@Getter
public class AlreadyHaveSignatureException extends RuntimeException{
    private final String NAME;

    public AlreadyHaveSignatureException(String message) {
        super(message);
        NAME = "ExistsSignatureException";
    }
}
