package com.blocker.blocker_server.commons.response.response_code;

import com.blocker.blocker_server.commons.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageResponseCode implements SuccessCode {

    POST_IMAGE(HttpStatus.CREATED, "이미지 저장에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
