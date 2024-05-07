package com.chatapp.kakaka.domain.chat.controller;


import com.chatapp.kakaka.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chat/rooms/{senderId}/{receiverId}")
    public Long getChatRoomId(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return chatRoomService.getChatRoom(senderId, receiverId).getId();
    }
}
