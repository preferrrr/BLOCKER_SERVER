package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class IsNotCancelingCancelContract extends RuntimeException {
    private final String NAME;

    public IsNotCancelingCancelContract(String message) {
        super(message);
        NAME = "IsNotCancelingCancelContract";
    }
}
