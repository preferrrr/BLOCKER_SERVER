package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CancelSignResponseCode implements SuccessCode{

    POST_CANCEL_SIGN(HttpStatus.CREATED, "파기 계약 진행에 성공헀습니다."),
    SIGN_CANCEL_CONTRACT(HttpStatus.OK, "파기 계약 서명에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
