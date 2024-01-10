package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class IsNotCanceledCancelContract extends RuntimeException {
    private final String NAME;

    public IsNotCanceledCancelContract(String message) {
        super(message);
        NAME = "IsNotCanceledCancelContract";
    }
}
