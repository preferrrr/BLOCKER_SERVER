package com.blocker.blocker_server.commons.kafka;

import com.blocker.blocker_server.chat.dto.response.SendMessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void send(String topic, KafkaMessage message) {
        kafkaTemplate.send(topic, message);
    }
}
