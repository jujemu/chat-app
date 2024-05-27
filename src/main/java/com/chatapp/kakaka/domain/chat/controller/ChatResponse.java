package com.chatapp.kakaka.domain.chat.controller;

import com.chatapp.kakaka.domain.chat.Chat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatResponse {

    private String chatRoomId;
    private List<ChatItem> chats;

    @Builder(access = AccessLevel.PRIVATE)
    public ChatResponse(String chatRoomId, List<ChatItem> chats) {
        this.chatRoomId = chatRoomId;
        this.chats = chats;
    }

    public static ChatResponse of(List<Chat> chats) {
        if (chats.isEmpty()) return null;
        if (chats.stream().anyMatch(chat -> !chats
                .getFirst().getChatRoomId().equals(chat.getChatRoomId())))
            throw new IllegalArgumentException("give me chats in only one chat room.");

        return ChatResponse.builder()
                .chatRoomId(
                        chats.getFirst().getId()
                )
                .chats(chats.stream()
                        .map(ChatItem::of).toList())
                .build();
    }
}
