package com.blocker.blocker_server.chat.controller;

import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.chat.dto.request.ChatMessage;
import com.blocker.blocker_server.chat.dto.request.CreateChatRoomRequestDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**메시지 전송*/
    @MessageMapping("/{roomId}")
    public void sendMessage(@Header("Authorization") String token, @DestinationVariable Long roomId, ChatMessage chatMessage) {

        chatService.sendMessage(token, roomId, chatMessage);

    }

    /**채팅방 생성
     * /chatrooms*/
    @PostMapping("/chatrooms")
    public ResponseEntity<HttpStatus> createRoom(@AuthenticationPrincipal User user, @RequestBody CreateChatRoomRequestDto createChatRoomRequestDto)  {

        createChatRoomRequestDto.validateFieldsNotNull();

        chatService.createChatRoom(user, createChatRoomRequestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**채팅방 리스트 조회*/
    @GetMapping("/chatrooms")
    public ResponseEntity<List<GetChatRoomListDto>> getChatRooms(@AuthenticationPrincipal User user) {

        List<GetChatRoomListDto> response = chatService.getChatRooms(user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}

