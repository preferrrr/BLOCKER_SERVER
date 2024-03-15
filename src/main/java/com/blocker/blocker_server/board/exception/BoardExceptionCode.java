package com.blocker.blocker_server.board.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Getter
public enum BoardExceptionCode implements ExceptionCode  {

    BOARD_NOT_FOUND(NOT_FOUND, "존재하지 않는 게시글입니다."),
    UNAUTHORIZED_DELETE(FORBIDDEN, "게시글을 삭제할 권한이 없습니다."),
    UNAUTHORIZED_MODIFY(FORBIDDEN, "게시글을 수정할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
