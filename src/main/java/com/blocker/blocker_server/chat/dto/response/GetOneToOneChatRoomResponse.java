package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetOneToOneChatRoomResponse {
    private Long chatRoomId;

    @Builder
    private GetOneToOneChatRoomResponse(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public static GetOneToOneChatRoomResponse of(Long chatRoomId) {
        return GetOneToOneChatRoomResponse.builder()
                .chatRoomId(chatRoomId)
                .build();
    }
}
