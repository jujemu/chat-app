package com.chatapp.kakaka.domain.chat.controller.dto;

import com.chatapp.kakaka.domain.chat.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String sender;
    private String content;
    private Long chatRoom;
    private ChatType type;
}
