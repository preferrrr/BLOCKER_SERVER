package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class IsNotProceedContractException extends RuntimeException{
    private final String NAME;

    public IsNotProceedContractException(String message) {
        super(message);
        NAME = "IsNotProceedContractException";
    }
}
