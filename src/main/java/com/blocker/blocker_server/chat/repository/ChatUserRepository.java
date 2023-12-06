package com.blocker.blocker_server.chat.repository;

import com.blocker.blocker_server.chat.domain.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
}
