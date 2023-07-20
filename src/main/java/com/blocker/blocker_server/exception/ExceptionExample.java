package com.blocker.blocker_server.exception;

import lombok.Getter;

public class ExceptionExample extends RuntimeException{
    @Getter
    private final String NAME;

    public ExceptionExample(String msg) {
        super(msg);
        NAME = "example";
    }
}
