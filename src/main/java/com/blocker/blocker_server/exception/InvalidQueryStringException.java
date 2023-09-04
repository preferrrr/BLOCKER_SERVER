package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class InvalidQueryStringException extends RuntimeException{
    private final String NAME;

    public InvalidQueryStringException(String message) {
        super(message);
        NAME = "InvalidQueryStringException";
    }
}
