package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardResponseCode implements SuccessCode {
    GET_BOARD_LIST(HttpStatus.OK, "게시글 리스트 조회에 성공했습니다."),
    GET_BOARD(HttpStatus.OK, "게시글 상세 조회에 성공했습니다."),
    POST_BOARD(HttpStatus.CREATED, "게시글 작성에 성공헀습니다."),
    DELETE_BOARD(HttpStatus.OK, "게시글 삭제에 성공했습니다."),
    MODIFY_BOARD(HttpStatus.OK, "게시글 수정에 성공헀습니다."),
    GET_MY_BOARD_LIST(HttpStatus.OK, "내가 작성한 게시글 리스트 조회에 성공헀습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
