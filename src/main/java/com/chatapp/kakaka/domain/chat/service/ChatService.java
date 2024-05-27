package com.chatapp.kakaka.domain.chat.service;

import com.chatapp.kakaka.domain.chat.Chat;
import com.chatapp.kakaka.domain.chat.controller.ChatResponse;
import com.chatapp.kakaka.domain.chat.controller.dto.ChatMessage;
import com.chatapp.kakaka.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public void send(ChatMessage message, LocalDateTime now) {
        Chat chat = newChat(message, now);
        chatRepository.save(chat);
    }

    public ChatResponse showChats(Long chatRoomId) {
        List<Chat> chats = chatRepository.findByChatRoomId(chatRoomId);
        return ChatResponse.of(chats);
    }

    private Chat newChat(ChatMessage message, LocalDateTime now) {
        return Chat.builder()
                .chatRoomId(message.getChatRoom())
                .sender(message.getSender())
                .content(message.getContent())
                .type(message.getType())
                .createdAt(now)
                .build();
    }
}
