package com.blocker.blocker_server.board.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.board.exception.BoardExceptionCode.UNAUTHORIZED_DELETE;

public class UnauthorizedDeleteBoardException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public UnauthorizedDeleteBoardException() {
        super(UNAUTHORIZED_DELETE.getMessage());
        this.exceptionCode = UNAUTHORIZED_DELETE;
    }
}
