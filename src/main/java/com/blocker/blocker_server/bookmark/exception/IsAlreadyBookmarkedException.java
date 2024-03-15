package com.blocker.blocker_server.bookmark.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.bookmark.exception.BookmarkExceptionCode.IS_ALREADY_BOOKMARKED;

public class IsAlreadyBookmarkedException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsAlreadyBookmarkedException() {
        super(IS_ALREADY_BOOKMARKED.getMessage());
        this.exceptionCode = IS_ALREADY_BOOKMARKED;
    }
}
