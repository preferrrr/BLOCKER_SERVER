package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class CancelContractNotFoundException extends RuntimeException{
    private final String NAME;

    public CancelContractNotFoundException(String message) {
        super(message);
        NAME = "CancelContractNotFoundException";
    }
}
