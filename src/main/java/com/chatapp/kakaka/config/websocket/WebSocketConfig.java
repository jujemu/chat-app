package com.chatapp.kakaka.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static String PUBLISHER_URL = "/pub";
    public static String SUBSCRIPTION_URL = "/sub";

    /*
        WebSocket 연결을 설정하고 클라이언트가 STOMP 프로토콜을 사용하여 서버와 통신할 수 있도록 한다.
     */
    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");
    }

    /*
    메세지 브로커를 구성한다.

    setApplicationDestinationPrefixes
    - 클라이언트에서 보낸 메세지의 목적지를 URL 의 prefix 를 필터링한다.
    - 목적지는 컨트롤러의 @MessageMapping()으로 지정하며, prefix 를 제외하여 설정한다.

    enableSimpleBroker
    - 클라이언트로부터 구독 요청을 받아 메모리에 저장
    - 목적지와 일치한 클라이언트에게 메세지를 브로드캐스팅
     */
    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(PUBLISHER_URL);
        registry.enableSimpleBroker(SUBSCRIPTION_URL);
    }

}
