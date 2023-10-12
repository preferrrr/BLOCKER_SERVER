package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class ExistsCancelSignException extends RuntimeException{
    private final String NAME;

    public ExistsCancelSignException(String message) {
        super(message);
        NAME = "ExistsCancelSignException";
    }
}
