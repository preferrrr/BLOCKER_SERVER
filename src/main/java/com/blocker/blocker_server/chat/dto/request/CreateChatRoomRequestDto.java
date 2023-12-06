package com.blocker.blocker_server.chat.dto.request;

import com.blocker.blocker_server.commons.exception.InvalidRequestParameterException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateChatRoomRequestDto {
    private List<String> chatUsers = new ArrayList<>();

    public void validateFieldsNotNull() {
        if(chatUsers.isEmpty())
            throw new InvalidRequestParameterException("Invalid chatUsers");
    }
}
