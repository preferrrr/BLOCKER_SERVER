package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class DuplicateSignException extends RuntimeException{
    private final String NAME;

    public DuplicateSignException(String message) {
        super(message);
        NAME = "DuplicateSignException";
    }
}
