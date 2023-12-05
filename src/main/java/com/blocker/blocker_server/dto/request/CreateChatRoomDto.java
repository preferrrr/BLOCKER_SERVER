package com.blocker.blocker_server.dto.request;

import com.blocker.blocker_server.exception.InvalidRequestParameterException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateChatRoomDto {
    private List<String> chatUsers = new ArrayList<>();

    public void validateFieldsNotNull() {
        if(chatUsers.isEmpty())
            throw new InvalidRequestParameterException("Invalid chatUsers");
    }
}
