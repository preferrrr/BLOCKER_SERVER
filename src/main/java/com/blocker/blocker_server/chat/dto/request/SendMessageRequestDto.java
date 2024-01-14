package com.blocker.blocker_server.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendMessageRequestDto {
    private String content;
}
