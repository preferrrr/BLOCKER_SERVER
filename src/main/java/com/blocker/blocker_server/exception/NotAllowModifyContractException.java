package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class NotAllowModifyContractException extends RuntimeException{
    private final String NAME;

    public NotAllowModifyContractException(String message) {
        super(message);
        NAME = "NotAllowedModifyContractException";
    }
}