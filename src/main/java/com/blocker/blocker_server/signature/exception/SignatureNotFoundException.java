package com.blocker.blocker_server.signature.exception;

import lombok.Getter;

@Getter
public class SignatureNotFoundException extends RuntimeException{
    private final String NAME;

    public SignatureNotFoundException(String message) {
        super(message);
        NAME = "SignatureNotFoundException";
    }
}
