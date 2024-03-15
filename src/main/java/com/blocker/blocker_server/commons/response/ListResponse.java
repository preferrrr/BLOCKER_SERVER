package com.blocker.blocker_server.commons.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ListResponse<T> extends BaseResponse {
    private final List<T> data;

    private ListResponse(final HttpStatus status, final List<T> data) {
        super(status);
        this.data = data;
    }

    public static <T> ListResponse<T> of(final HttpStatus status, final List<T> data) {
        return new ListResponse<>(status, data);
    }

    public static <T> ListResponse<T> ok(final List<T> data) {
        return new ListResponse<>(HttpStatus.OK, data);
    }
}
