package com.blocker.blocker_server.user.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.user.exception.UserExceptionCode.EMPTY_REFRESH_TOKEN;

public class EmptyRefreshTokenException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public EmptyRefreshTokenException() {
        super(EMPTY_REFRESH_TOKEN.getMessage());
        this.exceptionCode = EMPTY_REFRESH_TOKEN;
    }
}
