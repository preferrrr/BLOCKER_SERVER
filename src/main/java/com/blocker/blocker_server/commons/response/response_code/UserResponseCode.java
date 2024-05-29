package com.blocker.blocker_server.commons.response.response_code;

import com.blocker.blocker_server.commons.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserResponseCode implements SuccessCode {

    LOGIN(HttpStatus.OK, "로그인에 성공했습니다."),
    REISSUE_TOKEN(HttpStatus.OK, "토큰 재발급에 성공했습니다."),
    SEARCH_USERS(HttpStatus.OK, "유저 검색에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
