package com.blocker.blocker_server.bookmark.exception;

import lombok.Getter;

@Getter
public class IsNotBookmarkedException extends RuntimeException{
    private final String NAME;

    public IsNotBookmarkedException(String message) {
        super(message);
        NAME = "IsNotBookmarkedException";
    }
}
