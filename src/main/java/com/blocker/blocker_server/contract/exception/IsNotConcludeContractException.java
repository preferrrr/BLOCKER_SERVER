package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class IsNotConcludeContractException extends RuntimeException{
    private final String NAME;

    public IsNotConcludeContractException(String message) {
        super(message);
        NAME = "IsNotConcludeContractException";
    }
}
