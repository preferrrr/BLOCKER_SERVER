package com.blocker.blocker_server.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ChatUserId implements Serializable {

    private String email;

    private Long chatRoomId;

    @Builder
    public ChatUserId(String email, Long chatRoomId) {
        this.email = email;
        this.chatRoomId = chatRoomId;
    }

}
