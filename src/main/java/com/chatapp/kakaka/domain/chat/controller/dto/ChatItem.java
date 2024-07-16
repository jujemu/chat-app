package com.chatapp.kakaka.domain.chat.controller.dto;

import com.chatapp.kakaka.domain.chat.Chat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ChatItem {

    private String id;
    private String sender;
    private String content;
    private String createdAt;

    @Builder(access = AccessLevel.PROTECTED)
    private ChatItem(String id, String sender, String content, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt.format(
                DateTimeFormatter.ofPattern("kk:mm:ss")
        );
    }

    public static ChatItem of(Chat chat) {
        return ChatItem.builder()
                .id(chat.getId())
                .sender(chat.getSender())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
