package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AgreementSignResponseCode implements SuccessCode {

    POST_SIGN(HttpStatus.CREATED, "계약 진행에 성공했습니다."),
    SIGN_CONTRACT(HttpStatus.OK, "계약서 서명에 성공했습니다."),
    DELETE_CONTRACT(HttpStatus.OK, "진행 중 계약 파기에 성공헀습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
