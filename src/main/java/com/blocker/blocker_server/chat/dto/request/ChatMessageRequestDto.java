package com.blocker.blocker_server.chat.dto.request;

import lombok.Getter;

@Getter
public class ChatMessageRequestDto {
    private String content;
    private String sender;

    public void setSender(String sender) {
        this.sender = sender;
    }
}
