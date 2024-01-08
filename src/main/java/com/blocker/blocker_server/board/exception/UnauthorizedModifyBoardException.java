package com.blocker.blocker_server.board.exception;

import lombok.Getter;

@Getter
public class UnauthorizedModifyBoardException extends RuntimeException{
    private final String NAME;

    public UnauthorizedModifyBoardException(String message) {
        super(message);
        NAME = "UnauthorizedDeleteBoardException";
    }

}
