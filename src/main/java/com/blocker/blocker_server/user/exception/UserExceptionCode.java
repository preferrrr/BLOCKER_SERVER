package com.blocker.blocker_server.user.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserExceptionCode implements ExceptionCode {

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    EMPTY_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 null 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
