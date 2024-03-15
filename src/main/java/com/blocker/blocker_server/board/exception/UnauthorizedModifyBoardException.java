package com.blocker.blocker_server.board.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.board.exception.BoardExceptionCode.UNAUTHORIZED_MODIFY;

public class UnauthorizedModifyBoardException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public UnauthorizedModifyBoardException() {
        super(UNAUTHORIZED_MODIFY.getMessage());
        this.exceptionCode = UNAUTHORIZED_MODIFY;
    }
}
