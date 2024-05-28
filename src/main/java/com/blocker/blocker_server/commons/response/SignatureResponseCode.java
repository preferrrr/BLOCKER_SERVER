package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SignatureResponseCode implements SuccessCode {

    POST_SIGNATURE(HttpStatus.CREATED, "전자 서명 등록에 성공했습니다."),
    MODIFY_SIGNATURE(HttpStatus.OK, "전자 서명 수정에 성공했습니다."),
    GET_SIGNATURE(HttpStatus.OK, "전자 서명 조회에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
