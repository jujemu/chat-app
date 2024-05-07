package com.chatapp.kakaka.domain.chat.repository;

import com.chatapp.kakaka.domain.chat.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
