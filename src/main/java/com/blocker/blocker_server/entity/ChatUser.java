package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "CHAT_USER")
@DynamicInsert
@NoArgsConstructor
public class ChatUser {

    @EmbeddedId
    private ChatUserId id;

    @MapsId("email")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("chatRoomId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatUser(User user, ChatRoom chatRoom) {
        ChatUserId id = ChatUserId.builder()
                .chatRoomId(chatRoom.getChatRoomID())
                .email(user.getEmail())
                .build();

        this.id = id;
        this.chatRoom = chatRoom;
        this.user = user;
    }


}
