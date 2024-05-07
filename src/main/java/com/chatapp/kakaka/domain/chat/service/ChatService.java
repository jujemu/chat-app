package com.chatapp.kakaka.domain.chat.service;

import com.chatapp.kakaka.domain.chat.Chat;
import com.chatapp.kakaka.domain.chat.controller.dto.ChatMessage;
import com.chatapp.kakaka.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public void send(ChatMessage message, LocalDateTime now) {
        Chat chat = newChat(message, now);
        chatRepository.save(chat);
    }

    private Chat newChat(ChatMessage message, LocalDateTime now) {
        return Chat.builder()
                .chatRoom(message.getChatRoom())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .content(message.getContent())
                .createdAt(now)
                .build();
    }
}
