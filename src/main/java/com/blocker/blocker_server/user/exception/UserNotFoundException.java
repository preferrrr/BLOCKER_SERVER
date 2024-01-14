package com.blocker.blocker_server.user.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{
    private final String NAME;

    public UserNotFoundException(String message) {
        super(message);
        NAME = "UserNotFoundException";
    }
}
