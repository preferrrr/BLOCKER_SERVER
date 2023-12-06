package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendMessageResponseDto {
    private String sender;
    private String content;

    public void setSender(String sender) {
        this.sender = sender;
    }
}
