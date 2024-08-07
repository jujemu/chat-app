package com.chatapp.kakaka.config.redis;

import com.chatapp.kakaka.config.redis.message.MessageDto;
import com.chatapp.kakaka.config.sse.SseEmitters;
import com.chatapp.kakaka.domain.friend.service.FriendService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.CommonErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static com.chatapp.kakaka.config.redis.message.PubSubMessage.FRIEND_REQUEST;
import static com.chatapp.kakaka.config.redis.message.PubSubMessage.REQUEST_ACCEPT;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscribeListener implements MessageListener {

    private final RedisTemplate<String, Object> template;
    private final ObjectMapper objectMapper;
    private final SseEmitters emitters;

    private final FriendService friendService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("여기 왔다. 리스너");
            String publishMessage = template.getStringSerializer().deserialize(message.getBody());
            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);
            sendEventOfRequest(messageDto);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException occurs in redis subscription.");
        }
    }

    private void sendEventOfRequest(MessageDto messageDto) {
        String myName = messageDto.getSender();
        String receiverName = messageDto.getReceiver();
        String eventName = messageDto.getType();
        Long eventId = getEventId(messageDto);

        try {
            log.info("여기까지도 왔다.");
            if (!emitters.getEmitters().containsKey(receiverName))
                throw new IllegalArgumentException("연결되지 않은 text/stream 으로 알림을 전송하고 있습니다.");
            SseEmitter emitter = emitters.getEmitters().get(receiverName);
            emitter.send(
                    SseEmitter.event()
                            .id(String.valueOf(eventId))
                            .name(eventName)
                            .data(myName)
            );
            log.info("{} 알림을 전송했습니다.", receiverName);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Long getEventId(MessageDto messageDto) {
        if (messageDto.getType().equals(FRIEND_REQUEST.getText()))
            return friendService.sendRequest(messageDto.getSender(), messageDto.getReceiver());
        else if (messageDto.getType().equals(REQUEST_ACCEPT.getText()))
            return friendService.acceptRequest(messageDto.getSender(), messageDto.getReceiver());
        throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
    }
}
