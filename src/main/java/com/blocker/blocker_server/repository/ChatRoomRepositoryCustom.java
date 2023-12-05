package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.ChatRoom;
import com.blocker.blocker_server.entity.User;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findChatRoomsByUser(User user);
}
