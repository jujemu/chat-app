package com.chatapp.kakaka.domain.chat;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat")
public class Chat {

    private String id;
    private Long chatRoomId;
    private String sender;
    private String content;
    private ChatType type;
    private LocalDateTime createdAt;

    @Builder
    public Chat(Long chatRoomId, String sender, String content, ChatType type, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.createdAt = createdAt;
    }
}
