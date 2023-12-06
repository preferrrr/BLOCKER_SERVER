package com.blocker.blocker_server.commons.exception;

import lombok.Getter;

@Getter
public class IsProceedContractException extends RuntimeException{
    private final String NAME;

    public IsProceedContractException(String message) {
        super(message);
        NAME = "IsProceedContractException";
    }
}

