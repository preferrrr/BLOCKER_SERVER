package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
}
