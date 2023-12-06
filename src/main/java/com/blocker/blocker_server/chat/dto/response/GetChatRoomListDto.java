package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetChatRoomListDto {
    private Long chatRoomId;
    private String lastChat;
    private LocalDateTime lastChatTime;
}
