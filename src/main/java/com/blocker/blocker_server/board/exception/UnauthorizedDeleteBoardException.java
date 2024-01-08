package com.blocker.blocker_server.board.exception;

import lombok.Getter;

@Getter
public class UnauthorizedDeleteBoardException extends RuntimeException{
    private final String NAME;

    public UnauthorizedDeleteBoardException(String message) {
        super(message);
        NAME = "UnauthorizedDeleteBoardException";
    }

}
