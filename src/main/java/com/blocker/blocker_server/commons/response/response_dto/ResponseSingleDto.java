package com.blocker.blocker_server.commons.response.response_dto;

import lombok.Getter;

@Getter
public class ResponseSingleDto<T> {

    private T data;
    private String message;

    public ResponseSingleDto(T data, String message) {
        this.data = data;
        this.message = message;
    }

}
