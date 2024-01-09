package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class ContractNotFoundException extends RuntimeException{
    private final String NAME;

    public ContractNotFoundException(String message) {
        super(message);
        NAME = "ContractNotFoundException";
    }
}
