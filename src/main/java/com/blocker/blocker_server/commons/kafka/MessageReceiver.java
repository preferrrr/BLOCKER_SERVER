package com.blocker.blocker_server.commons.kafka;

import com.blocker.blocker_server.chat.dto.response.SendMessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageReceiver {

    private final SimpMessagingTemplate template;

    @KafkaListener(topics = KafkaChatConstant.KAFKA_TOPIC, groupId = KafkaChatConstant.KAFKA_GROUP)
    public void receiveMessage(KafkaMessage message) {

        SendMessageResponseDto dto = SendMessageResponseDto.builder()
                .content(message.getContent())
                .sender(message.getSender())
                .build();

        template.convertAndSend("/sub/" + message.getRoomId(), dto);

    }
}
