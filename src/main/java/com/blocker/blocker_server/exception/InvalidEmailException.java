package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class InvalidEmailException extends RuntimeException{
    private final String NAME;

    public InvalidEmailException(String message) {
        super(message);
        NAME = "InvalidEmailException";
    }
}
