package com.blocker.blocker_server.exception;

import lombok.Getter;

@Getter
public class ExistsProceededContractException extends RuntimeException{
    private final String NAME;

    public ExistsProceededContractException(String message) {
        super(message);
        NAME = "ExistsProceededContractException";
    }
}
