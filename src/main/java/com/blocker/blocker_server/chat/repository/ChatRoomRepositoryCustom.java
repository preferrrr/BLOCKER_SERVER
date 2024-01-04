package com.blocker.blocker_server.chat.repository;

import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.user.domain.User;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findChatRoomsByUser(User user);

    Long findOneToOneChatRoomByUsers(String userA, String userB);

}
