package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class DuplicateContractException extends RuntimeException{
    private final String NAME;

    public DuplicateContractException(String message) {
        super(message);
        NAME = "DuplicateContractException";
    }
}
