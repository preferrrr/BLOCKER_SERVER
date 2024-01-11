package com.blocker.blocker_server.sign.exception;

import lombok.Getter;

@Getter
public class IsAlreadyCancelingException extends RuntimeException {
    private final String NAME;

    public IsAlreadyCancelingException(String message) {
        super(message);
        NAME = "IsAlreadyCancelingException";
    }
}
