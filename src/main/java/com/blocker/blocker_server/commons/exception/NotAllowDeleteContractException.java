package com.blocker.blocker_server.commons.exception;

import lombok.Getter;

@Getter
public class NotAllowDeleteContractException extends RuntimeException{
    private final String NAME;

    public NotAllowDeleteContractException(String message) {
        super(message);
        NAME = "NotAllowDeleteContractException";
    }
}