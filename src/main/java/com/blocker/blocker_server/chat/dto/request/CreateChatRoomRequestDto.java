package com.blocker.blocker_server.chat.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateChatRoomRequestDto {
    private List<String> chatUsers = new ArrayList<>();

    @Builder
    private CreateChatRoomRequestDto(List<String> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public void validateFieldsNotNull() {
        if(chatUsers.isEmpty())
            throw new InvalidRequestParameterException("Invalid chatUsers");
    }
}
