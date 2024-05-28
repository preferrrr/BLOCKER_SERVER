package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserResponseCode implements SuccessCode {

    REISSUE_TOKEN(HttpStatus.OK, "토큰 재발급에 성공했습니다."),
    SEARCH_USERS(HttpStatus.OK, "유저 검색에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
