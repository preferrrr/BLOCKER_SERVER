package com.blocker.blocker_server.chat.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.chat.exception.ChatExceptionCode.CHATROOM_NOT_FOUND;

public class ChatRoomNotFoundException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public ChatRoomNotFoundException() {
        super(CHATROOM_NOT_FOUND.getMessage());
        this.exceptionCode = CHATROOM_NOT_FOUND;
    }
}
