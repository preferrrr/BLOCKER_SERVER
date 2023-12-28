package com.blocker.blocker_server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOneToOneChatRoomResponse {
    private Long chatRoomId;
}
