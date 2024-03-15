package com.blocker.blocker_server.chat.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ChatExceptionCode implements ExceptionCode {

    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다.."),
    IS_NOT_PARTICIPANT(HttpStatus.BAD_REQUEST, "채팅 참여자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
