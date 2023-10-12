package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class NotConcludeContractException extends RuntimeException{
    private final String NAME;

    public NotConcludeContractException(String msg) {
        super(msg);
        NAME = "NotConcludeContractException";
    }
}
