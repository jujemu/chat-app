package com.chatapp.kakaka.domain.chat.controller;

import com.chatapp.kakaka.domain.chat.controller.dto.ChatMessage;
import com.chatapp.kakaka.domain.chat.controller.dto.ChatResponse;
import com.chatapp.kakaka.domain.chat.service.ChatService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.chatapp.kakaka.config.websocket.WebSocketConfig.SUBSCRIPTION_URL;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
    MessageMapping: 클라이언트에서 웹소켓으로 보낸 메세지를 라우팅할 목적지를 설정한다. 발행할 메세지이기 때문에 /pub prefix 를 갖는다.
    SendTo: 메서드의 반환 값을 메세지로 변환하고 지정된 목적지로 전달한다.
    Payload: 웹소켓을 이용한 통신은 HTTP 와 다르게 "메세지"를 전달한다. 메세지의 payload 를 객체로 매핑한다.
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        LocalDateTime now = LocalDateTime.now();
        String destinationURL = SUBSCRIPTION_URL + "/chat/room/" + chatMessage.getChatRoom();
        Map<String, Object> headers = getHeaders(chatMessage, now);

        chatService.send(chatMessage, now);
        simpMessageSendingOperations.convertAndSend(destinationURL, chatMessage.getContent(), headers);
    }

    @GetMapping("/chats")
    public ChatResponse showChats(@RequestParam String myName, @RequestParam Long chatRoomId, Authentication authentication) {
        isRequestFromMe(authentication, myName);
        return chatService.showChats(chatRoomId);
    }

    private void isRequestFromMe(Authentication authentication, String myName) {
        User user = (User) authentication.getPrincipal();
        if (!user.getUsername().equals(myName))
            throw new RestApiException(UserErrorCode.UNAUTHORIZED);
    }

    private Map<String, Object> getHeaders(ChatMessage chatMessage, LocalDateTime now) {
        String nowStr = now.format(DateTimeFormatter.ofPattern("kk:mm:ss"));
        return new HashMap<>() {{
            put("sender", chatMessage.getSender());
            put("createdAt", nowStr);
            put("type", chatMessage.getType());
        }};
    }
}
