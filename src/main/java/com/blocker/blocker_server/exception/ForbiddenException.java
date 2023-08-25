package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
    private final String NAME;

    public ForbiddenException(String message) {
        super(message);
        NAME = "ForbiddenException";
    }
}
