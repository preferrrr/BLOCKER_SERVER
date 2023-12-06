package com.blocker.blocker_server.chat.repository;

import com.blocker.blocker_server.chat.mongo.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
