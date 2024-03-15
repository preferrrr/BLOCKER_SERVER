package com.blocker.blocker_server.chat.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateChatRoomRequestDto {

    @Size(min = 1, message = "채팅 상대방은 1명 이상이어야 합니다.")
    private List<String> chatUsers = new ArrayList<>();

    @Builder
    private CreateChatRoomRequestDto(List<String> chatUsers) {
        this.chatUsers = chatUsers;
    }
}
