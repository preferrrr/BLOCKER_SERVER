package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.request.CreateChatRoomDto;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@Header("Authorization") String token, @DestinationVariable Long roomId, ChatMessage chatMessage) {

        chatService.sendMessage(token, roomId, chatMessage);

    }

    @PostMapping("/chatrooms")
    public ResponseEntity<HttpStatus> createRoom(@AuthenticationPrincipal User user, @RequestBody CreateChatRoomDto createChatRoomDto)  {

        createChatRoomDto.validateFieldsNotNull();

        chatService.createChatRoom(user, createChatRoomDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}

