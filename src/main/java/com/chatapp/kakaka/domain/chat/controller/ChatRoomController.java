package com.chatapp.kakaka.domain.chat.controller;


import com.chatapp.kakaka.domain.chat.service.ChatRoomService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chat/rooms")
    @ResponseStatus(HttpStatus.OK)
    public Long getChatRoomId(@RequestParam String myName, @RequestParam String friendName, Authentication authentication) {
        isRequestFromMe(authentication, myName);
        return chatRoomService.getChatRoom(myName, friendName).getId();
    }

    private void isRequestFromMe(Authentication authentication, String myName) {
        User user = (User) authentication.getPrincipal();
        if (!user.getUsername().equals(myName))
            throw new RestApiException(UserErrorCode.UNAUTHORIZED);
    }
}
