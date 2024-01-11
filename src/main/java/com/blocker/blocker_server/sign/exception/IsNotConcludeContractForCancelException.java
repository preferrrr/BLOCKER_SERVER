package com.blocker.blocker_server.sign.exception;

import lombok.Getter;

@Getter
public class IsNotConcludeContractForCancelException extends RuntimeException {
    private final String NAME;

    public IsNotConcludeContractForCancelException(String message) {
        super(message);
        NAME = "IsNotConcludeContractForCancelException";
    }

}
