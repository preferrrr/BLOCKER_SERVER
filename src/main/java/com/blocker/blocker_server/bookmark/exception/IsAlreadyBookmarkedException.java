package com.blocker.blocker_server.bookmark.exception;

import lombok.Getter;

@Getter
public class IsAlreadyBookmarkedException extends RuntimeException{
    private final String NAME;

    public IsAlreadyBookmarkedException(String message) {
        super(message);
        NAME = "IsAlreadyBookmarkedException";
    }
}
