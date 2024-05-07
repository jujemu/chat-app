package com.chatapp.kakaka.domain.chat;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(collection = "chat")
public class Chat {

    private String id;
    private Long chatRoom;
    private String sender;
    private String receiver;
    private String content;

    private LocalDateTime createdAt;

    @Builder
    public Chat(Long chatRoom, String sender, String receiver, String content, LocalDateTime createdAt) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.createdAt = createdAt;
    }
}
