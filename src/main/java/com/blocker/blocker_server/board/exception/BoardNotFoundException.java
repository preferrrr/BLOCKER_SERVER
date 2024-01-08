package com.blocker.blocker_server.board.exception;

import lombok.Getter;

@Getter
public class BoardNotFoundException extends RuntimeException{
    private final String NAME;

    public BoardNotFoundException(String message) {
        super(message);
        NAME = "BoardNotFoundException";
    }
}
