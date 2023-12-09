package com.blocker.blocker_server.commons.kafka;

import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.response.SendMessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageReceiver {
    private final SimpMessagingTemplate template;

    @KafkaListener(topics = "test", groupId = "test")
    public void receiveMessage(SendMessageResponseDto message) {
        log.info("token: " );
        log.info("ChatRoomId: " + message.getRoomId());

        template.convertAndSend("/sub/" + message.getRoomId(), message);
    }
}
