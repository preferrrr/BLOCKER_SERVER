package com.blocker.blocker_server.chat.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_message")
@Getter
public class ChatMessage {

    @Id
    private String id;

    private Long chatRoomId;
    private String sender;
    private String senderEmail;
    private String content;
    private LocalDateTime sendAt;

    @Builder
    private ChatMessage(Long chatRoomId, String sender, String senderEmail, String content, LocalDateTime sendAt) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.senderEmail = senderEmail;
        this.content = content;
        this.sendAt = sendAt;
    }

    public static ChatMessage create(Long chatRoomId, String sender, String senderEmail, String content, LocalDateTime sendAt) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .sender(sender)
                .senderEmail(senderEmail)
                .content(content)
                .sendAt(sendAt)
                .build();
    }
}
