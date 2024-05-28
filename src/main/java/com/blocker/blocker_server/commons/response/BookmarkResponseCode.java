package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookmarkResponseCode implements SuccessCode {

    POST_BOOKMARK(HttpStatus.CREATED, "북마크 등록에 성공했습니다."),
    DELETE_BOOKMARK(HttpStatus.OK, "북마크 삭제에 성공했습니다."),
    GET_BOOKMARK_BOARDS(HttpStatus.OK, "북마크한 게시글 조회에 성공헀습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
