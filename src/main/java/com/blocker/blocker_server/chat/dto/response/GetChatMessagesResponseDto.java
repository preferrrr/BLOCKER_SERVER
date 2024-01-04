package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetChatMessagesResponseDto {
    private String sender;
    private String content;
    private LocalDateTime sendAt;

    @Builder
    private GetChatMessagesResponseDto(String sender, String content, LocalDateTime sendAt) {
        this.sender = sender;
        this.content = content;
        this.sendAt = sendAt;
    }

    public static GetChatMessagesResponseDto of(String sender, String content, LocalDateTime sendAt) {
        return GetChatMessagesResponseDto.builder()
                .sender(sender)
                .content(content)
                .sendAt(sendAt)
                .build();
    }
}
