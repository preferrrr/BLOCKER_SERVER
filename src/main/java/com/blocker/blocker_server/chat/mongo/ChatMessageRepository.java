package com.blocker.blocker_server.chat.mongo;

import com.blocker.blocker_server.chat.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    @Query("{'chatRoomId' : ?0}")
    List<ChatMessage> findByChatRoomId(Long chatRoomId, Pageable pageable);
}
