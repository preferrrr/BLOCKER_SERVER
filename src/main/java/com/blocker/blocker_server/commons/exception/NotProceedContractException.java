package com.blocker.blocker_server.commons.exception;

import lombok.Getter;

@Getter
public class NotProceedContractException extends RuntimeException{
    @Getter
    private final String NAME;

    public NotProceedContractException(String msg) {
        super(msg);
        NAME = "NotProceedContractException";
    }
}
