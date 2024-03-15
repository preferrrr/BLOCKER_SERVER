package com.blocker.blocker_server.signature.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SignatureExceptionCode implements ExceptionCode {

    ALREADY_HAVE_SIGNATURE(HttpStatus.CONFLICT, "이미 전자 서명을 등록했습니다."),
    SIGNATURE_NOT_FOUND(HttpStatus.NOT_FOUND, "전자 서명을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
