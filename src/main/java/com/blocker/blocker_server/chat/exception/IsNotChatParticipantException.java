package com.blocker.blocker_server.chat.exception;

import com.blocker.blocker_server.commons.exception.ExceptionCode;
import lombok.Getter;

import static com.blocker.blocker_server.chat.exception.ChatExceptionCode.IS_NOT_CHATROOM_PARTICIPANT;

public class IsNotChatParticipantException extends RuntimeException{
    @Getter
    private final ExceptionCode exceptionCode;

    public IsNotChatParticipantException() {
        super(IS_NOT_CHATROOM_PARTICIPANT.getMessage());
        this.exceptionCode = IS_NOT_CHATROOM_PARTICIPANT;
    }
}
