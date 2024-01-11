package com.blocker.blocker_server.sign.exception;

import lombok.Getter;

@Getter
public class IsAlreadyCancelSignException extends RuntimeException {
    private final String NAME;

    public IsAlreadyCancelSignException(String message) {
        super(message);
        NAME = "IsAlreadyCancelSignException";
    }
}
