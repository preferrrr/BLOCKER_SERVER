package com.blocker.blocker_server.chat.exception;

import lombok.Getter;

@Getter
public class ChatRoomNotFoundException extends RuntimeException{
    private final String NAME;

    public ChatRoomNotFoundException(String message) {
        super(message);
        NAME = "ChatRoomNotFoundException";
    }
}
