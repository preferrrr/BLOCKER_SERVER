package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class DuplicateBookmarkException extends RuntimeException{
    private final String NAME;

    public DuplicateBookmarkException(String message) {
        super(message);
        NAME = "DuplicateBookmarkException";
    }
}
