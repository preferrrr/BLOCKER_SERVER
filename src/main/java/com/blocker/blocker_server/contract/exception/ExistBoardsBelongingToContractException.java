package com.blocker.blocker_server.contract.exception;

import lombok.Getter;

@Getter
public class ExistBoardsBelongingToContractException extends RuntimeException {
    private final String NAME;

    public ExistBoardsBelongingToContractException(String message) {
        super(message);
        NAME = "ExistBoardsBelongingToContractException";
    }
}
