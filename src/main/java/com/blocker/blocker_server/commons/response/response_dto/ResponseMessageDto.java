package com.blocker.blocker_server.commons.response.response_dto;

import lombok.Getter;

@Getter
public class ResponseMessageDto {
    private String message;

    public ResponseMessageDto(String message) {
        this.message = message;
    }
}
