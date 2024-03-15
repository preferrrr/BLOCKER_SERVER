package com.blocker.blocker_server.bookmark.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum BookmarkExceptionCode implements ExceptionCode {

    IS_ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "이미 북마크한 게시글입니다."),
    IS_NOT_BOOKMARKED(HttpStatus.BAD_REQUEST, "북마크한 게시글이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
