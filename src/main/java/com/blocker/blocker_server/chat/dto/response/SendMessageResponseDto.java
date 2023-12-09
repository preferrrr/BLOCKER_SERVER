package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendMessageResponseDto {
    private String roomId;
    private String sender;
    private String content;

    public SendMessageResponseDto() {

    }
    public SendMessageResponseDto(String roomId, String sender, String content) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;

    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
