package com.blocker.blocker_server.commons.exception;

import lombok.Getter;

@Getter
public class NotCancelingContractException extends RuntimeException{
    private final String NAME;

    public NotCancelingContractException(String message) {
        super(message);
        NAME = "NotCancelingContractException";
    }
}