package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class ExistsSignatureException extends RuntimeException{
    private final String NAME;

    public ExistsSignatureException(String message) {
        super(message);
        NAME = "ExistsSignatureException";
    }
}
