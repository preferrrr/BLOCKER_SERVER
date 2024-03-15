package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SingleResponse<T> extends BaseResponse {

    private final T data;

    private SingleResponse(final HttpStatus status, final T data) {
        super(status);
        this.data = data;
    }

    public static <T> SingleResponse<T> of(final HttpStatus status, final T data) {
        return new SingleResponse<>(status, data);
    }

    public static <T> SingleResponse<T> ok(final T data) {
        return new SingleResponse<>(HttpStatus.OK, data);
    }
}
