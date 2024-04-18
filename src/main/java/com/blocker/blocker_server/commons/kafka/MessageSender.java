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

    public void send(String topic, String chatRoomId, KafkaMessage message) {
        kafkaTemplate.send(topic, chatRoomId, message);
    }
    //topic의 파티션이 여러 개일 때, 채팅방 메시지가 아무 파티션으로 간다면 메시지 순서가 보장되지 않음
    //그래서 chatRoomId로 해싱한 값이 파티션 키가 되어, 같은 채팅방의 메시지는 같은 파티션으로 보내지도록 한다.
}
