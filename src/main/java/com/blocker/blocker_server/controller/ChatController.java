package com.blocker.blocker_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/{roomId}")
    //@SendTo("/sub/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable Long roomId, ChatMessage chatMessage) {
        System.out.println("roomId : " + roomId);

        System.out.println("message : " + chatMessage.getContent());
        simpMessagingTemplate.convertAndSend("/sub/" + roomId, chatMessage);

        return chatMessage;
    }

}

