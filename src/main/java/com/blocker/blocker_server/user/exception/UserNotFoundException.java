package com.blocker.blocker_server.user.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.user.exception.UserExceptionCode.USER_NOT_FOUND;

public class UserNotFoundException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public UserNotFoundException() {
        super(USER_NOT_FOUND.getMessage());
        this.exceptionCode = USER_NOT_FOUND;
    }
}
