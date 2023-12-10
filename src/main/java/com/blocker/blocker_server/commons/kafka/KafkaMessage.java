package com.blocker.blocker_server.commons.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage {

    private String roomId;
    private String sender;
    private String content;

}
