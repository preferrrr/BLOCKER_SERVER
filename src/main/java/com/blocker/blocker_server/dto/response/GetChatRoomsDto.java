package com.blocker.blocker_server.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetChatRoomsDto {
    private Long chatRoomId;
    private String lastChat;
    private LocalDateTime lastChatTime;
}
