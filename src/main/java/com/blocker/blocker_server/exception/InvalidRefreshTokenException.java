package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class InvalidRefreshTokenException extends RuntimeException{
    private final String NAME;

    public InvalidRefreshTokenException(String message) {
        super(message);
        NAME = "InvalidRefreshTokenException";
    }
}
