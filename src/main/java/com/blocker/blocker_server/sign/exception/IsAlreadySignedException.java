package com.blocker.blocker_server.sign.exception;

import lombok.Getter;

@Getter
public class IsAlreadySignedException extends RuntimeException {
    private final String NAME;

    public IsAlreadySignedException(String message) {
        super(message);
        NAME = "IsAlreadySignedException";
    }
}
