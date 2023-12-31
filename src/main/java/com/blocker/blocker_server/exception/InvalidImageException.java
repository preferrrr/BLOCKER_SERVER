package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class InvalidImageException extends RuntimeException{
    private final String NAME;

    public InvalidImageException(String message) {
        super(message);
        NAME = "InvalidImageException";
    }
}
