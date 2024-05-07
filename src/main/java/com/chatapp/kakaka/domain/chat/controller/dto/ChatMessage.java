package com.chatapp.kakaka.domain.chat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

//    private MessageType type;
    private String sender;
    private String receiver;
    private String content;
    private Long chatRoom;
}
