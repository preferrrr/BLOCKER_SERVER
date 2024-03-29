package com.blocker.blocker_server.chat.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "CHAT_ROOM")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomID;

    @Column(name = "last_chat")
    private String lastChat;

    @Column(name = "last_chat_time")
    private LocalDateTime lastChatTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatUser> chatUsers = new ArrayList<>();

    @Builder private ChatRoom(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static ChatRoom create(LocalDateTime createdAt) {
        return ChatRoom.builder()
                .createdAt(createdAt)
                .build();
    }

    public void updateLastChat(String lastChat, LocalDateTime lastChatTime) {
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
    }


}
