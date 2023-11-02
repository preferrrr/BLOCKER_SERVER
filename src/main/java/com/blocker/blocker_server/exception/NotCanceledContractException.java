package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class NotCanceledContractException extends RuntimeException{
    private final String NAME;

    public NotCanceledContractException(String message) {
        super(message);
        NAME = "NotCanceledContractException";
    }
}

