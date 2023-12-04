package com.blocker.blocker_server.service;

import com.blocker.blocker_server.controller.ChatMessage;
import com.blocker.blocker_server.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    public void sendMessage(String token, Long roomId, ChatMessage message) {

        String tokenValue = token.substring(7);

        String email = jwtProvider.getEmail(tokenValue);

        //TODO: 참여자가 맞는지. ChatRoom에서 조회해야 함.
        //TODO: DB에 저장해야함.

        String userName = jwtProvider.getUsername(tokenValue);

        message.setSender(userName);

        simpMessagingTemplate.convertAndSend("/sub/" + roomId, message);

    }
}
