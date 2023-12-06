package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetChatMessagesResponseDto {
    private String sender;
    private String content;
    private LocalDateTime sendAt;
}
