package com.blocker.blocker_server.board.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.board.exception.BoardExceptionCode.BOARD_NOT_FOUND;

public class BoardNotFoundException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public BoardNotFoundException() {
        super(BOARD_NOT_FOUND.getMessage());
        this.exceptionCode = BOARD_NOT_FOUND;
    }
}
