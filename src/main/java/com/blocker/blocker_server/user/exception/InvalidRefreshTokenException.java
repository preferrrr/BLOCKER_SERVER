package com.blocker.blocker_server.user.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.chat.exception.ChatExceptionCode.CHATROOM_NOT_FOUND;
import static com.blocker.blocker_server.user.exception.UserExceptionCode.INVALID_REFRESH_TOKEN;

public class InvalidRefreshTokenException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public InvalidRefreshTokenException() {
        super(INVALID_REFRESH_TOKEN.getMessage());
        this.exceptionCode = INVALID_REFRESH_TOKEN;
    }
}
