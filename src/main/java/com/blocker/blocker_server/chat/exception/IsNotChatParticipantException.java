package com.blocker.blocker_server.chat.exception;

import lombok.Getter;

@Getter
public class IsNotChatParticipantException extends RuntimeException{
    private final String NAME;

    public IsNotChatParticipantException(String message) {
        super(message);
        NAME = "IsNotChatParticipantException";
    }
}
