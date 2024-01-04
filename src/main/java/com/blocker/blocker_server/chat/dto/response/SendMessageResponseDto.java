package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendMessageResponseDto {

    private String sender;
    private String content;

    @Builder
    private SendMessageResponseDto(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public static SendMessageResponseDto of(String sender, String content) {
        return SendMessageResponseDto.builder()
                .sender(sender)
                .content(content)
                .build();
    }
}
