package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetChatRoomListDto {
    private Long chatRoomId;
    private String lastChat;
    private LocalDateTime lastChatTime;

    @Builder
    private GetChatRoomListDto(Long chatRoomId, String lastChat, LocalDateTime lastChatTime) {
        this.chatRoomId = chatRoomId;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
    }

    public static GetChatRoomListDto of(Long chatRoomId, String lastChat, LocalDateTime lastChatTime) {
        return GetChatRoomListDto.builder()
                .chatRoomId(chatRoomId)
                .lastChat(lastChat)
                .lastChatTime(lastChatTime)
                .build();
    }
}
