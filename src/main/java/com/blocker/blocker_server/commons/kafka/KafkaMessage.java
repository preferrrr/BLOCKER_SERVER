package com.blocker.blocker_server.commons.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KafkaMessage {

    private String roomId;
    private String sender;
    private String content;

    @Builder
    private KafkaMessage(String roomId, String sender, String content) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
    }

    public static KafkaMessage create(String roomId, String sender, String content) {
        return KafkaMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .content(content)
                .build();
    }
}
