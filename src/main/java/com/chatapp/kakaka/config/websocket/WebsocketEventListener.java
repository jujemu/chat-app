package com.chatapp.kakaka.config.websocket;

import com.chatapp.kakaka.domain.chat.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebsocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("sender");
        Map<String, Object> headers = getHeaders(username, LocalDateTime.now());
        if (username != null) {
            messageTemplate.convertAndSend("/topic/public", "", headers);
        }
    }

    private Map<String, Object> getHeaders(String sender, LocalDateTime now) {
        String nowStr = now.format(DateTimeFormatter.ofPattern("kk:mm:ss"));
        return new HashMap<>() {{
            put("sender", sender);
            put("createdAt", nowStr);
            put("type", ChatType.DISCONNECTED);
        }};
    }
}
