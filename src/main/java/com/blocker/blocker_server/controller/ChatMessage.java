package com.blocker.blocker_server.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatMessage {
    private String content;
    private String sender;

    public void setSender(String sender) {
        this.sender = sender;
    }
}
