package com.chatapp.kakaka.domain.chat.repository;

import com.chatapp.kakaka.domain.chat.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {

    List<Chat> findByChatRoomId(Long chatRoomId);
}
