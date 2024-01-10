package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class IsNotCancelContractParticipant extends RuntimeException {
    private final String NAME;

    public IsNotCancelContractParticipant(String message) {
        super(message);
        NAME = "IsNotCancelContractParticipant";
    }
}
