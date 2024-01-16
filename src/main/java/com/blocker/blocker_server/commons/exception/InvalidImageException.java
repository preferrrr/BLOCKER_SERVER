package com.blocker.blocker_server.commons.exception;

import lombok.Getter;

@Getter
public class InvalidImageException extends RuntimeException{
    private final String NAME;

    public InvalidImageException() {
        NAME = "InvalidImageException";
    }
}
