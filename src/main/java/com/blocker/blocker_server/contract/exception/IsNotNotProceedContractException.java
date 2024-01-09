package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class IsNotNotProceedContractException extends RuntimeException{
    private final String NAME;

    public IsNotNotProceedContractException(String message) {
        super(message);
        NAME = "IsNotNotProceedContractException";
    }
}
