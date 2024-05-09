package com.chatapp.kakaka.domain.chat.controller;


import com.chatapp.kakaka.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chat/rooms/{senderName}/{receiverName}")
    @ResponseStatus(HttpStatus.OK)
    public Long getChatRoomId(@PathVariable String senderName, @PathVariable String receiverName) {
        return chatRoomService.getChatRoom(senderName, receiverName).getId();
    }
}
