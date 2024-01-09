package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class CannotModifyContractInConcludedStateException extends RuntimeException {
    private final String NAME;

    public CannotModifyContractInConcludedStateException(String message) {
        super(message);
        NAME = "CannotModifyContractInConcludedStateException";
    }
}
