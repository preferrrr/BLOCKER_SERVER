package com.blocker.blocker_server.chat.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_message")
@Getter
@Builder
public class ChatMessage {

    @Id
    private String id;

    private Long chatRoomId;
    private String sender;
    private String senderEmail;
    private String content;
    private LocalDateTime sendAt;
}
