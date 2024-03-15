package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class BaseResponse {

    private final HttpStatus status;
    private final LocalDateTime time = LocalDateTime.now();

    protected BaseResponse(final HttpStatus status) {
        this.status = status;
    }

    public static BaseResponse of(final HttpStatus status) {
        return new BaseResponse(status);
    }

    public static BaseResponse ok() {
        return new BaseResponse(HttpStatus.OK);
    }
}
