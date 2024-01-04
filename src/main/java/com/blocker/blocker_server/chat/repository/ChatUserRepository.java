package com.blocker.blocker_server.chat.repository;

import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);

    List<ChatUser> findByChatRoom(ChatRoom chatRoom);
}
