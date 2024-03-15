package com.blocker.blocker_server.contract.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.contract.exception.ContractExceptionCode.EXISTS_BOARD_BELONGING_TO_CONTRACT;

public class ExistBoardsBelongingToContractException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public ExistBoardsBelongingToContractException() {
        super(EXISTS_BOARD_BELONGING_TO_CONTRACT.getMessage());
        this.exceptionCode = EXISTS_BOARD_BELONGING_TO_CONTRACT;
    }
}
