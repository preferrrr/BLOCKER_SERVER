package com.blocker.blocker_server.bookmark.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.bookmark.exception.BookmarkExceptionCode.IS_NOT_BOOKMARKED;

public class IsNotBookmarkedException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotBookmarkedException() {
        super(IS_NOT_BOOKMARKED.getMessage());
        this.exceptionCode = IS_NOT_BOOKMARKED;
    }
}
