package com.blocker.blocker_server.chat.repository;

public interface ChatUserRepositoryCustom {
    Long findChatRoomByUsers(String userA, String userB);
}
