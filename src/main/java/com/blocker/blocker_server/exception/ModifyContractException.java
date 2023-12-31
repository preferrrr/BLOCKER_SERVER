package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class ModifyContractException extends RuntimeException{
    private final String NAME;

    public ModifyContractException(String message) {
        super(message);
        NAME = "ModifyContractException";
    }
}
