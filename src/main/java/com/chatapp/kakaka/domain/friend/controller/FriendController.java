package com.chatapp.kakaka.domain.friend.controller;

import com.chatapp.kakaka.config.redis.RedisPublisher;
import com.chatapp.kakaka.config.redis.RedisSubscribeListener;
import com.chatapp.kakaka.config.redis.message.MessageDto;
import com.chatapp.kakaka.config.sse.SseEmittersWithUsername;
import com.chatapp.kakaka.domain.friend.controller.dto.FriendListResponse;
import com.chatapp.kakaka.domain.friend.service.FriendService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.CommonErrorCode;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class FriendController {

    private final FriendService friendService;
    private final RedisPublisher redisPublisher;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisSubscribeListener redisSubscribeListener;
//    private final SseEmitters emitters;

    @GetMapping("/friend/all/{myName}")
    public FriendListResponse showAll(@PathVariable String myName) {
        return friendService.showAll(myName);
    }

    @GetMapping("/friend/requests")
    public FriendListResponse showRequests(@RequestParam String myName) {
        if (myName == null || myName.isBlank())
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        return friendService.showRequests(myName);
    }

    @GetMapping(value = "/friend/requests/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectRequest(@RequestParam String myName) {
        if (myName == null || myName.isBlank())
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);

        redisMessageListenerContainer.addMessageListener(
                redisSubscribeListener,
                ChannelTopic.of(myName)
        );

        // 클라이언트와 text/stream 연결된 직후에 메세지를 보내서 연결이 해제되지 않도록 한다.
        SseEmitter sseEmitter = new SseEmitter(1000L * 120);
        emitters.add(myName, sseEmitter);
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("ignore:connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sseEmitter;
    }

    @PostMapping("/friend/request/{myName}/{receiverName}")
    public void sendRequest(@PathVariable String myName, @PathVariable String receiverName, Authentication authentication) {
        isRequestFromMe(authentication, myName);
        Long eventId = friendService.sendRequest(myName, receiverName);
        redisPublisher.publish(
                ChannelTopic.of(receiverName),
                getMessage(eventId, myName, receiverName, "friendRequest")
        );
    }

    @PostMapping("/friend/request/accept/{myName}/{receiverName}")
    public void acceptRequest(@PathVariable String myName, @PathVariable String receiverName, Authentication authentication) {
        isRequestFromMe(authentication, myName);
        Long eventId = friendService.acceptRequest(myName, receiverName);
        redisPublisher.publish(
                ChannelTopic.of(receiverName),
                getMessage(eventId, myName, receiverName, "requestAccept")
        );
    }

    @PostMapping("/friend/request/deny/{myName}/{receiverName}")
    public void denyRequest(@PathVariable String myName, @PathVariable String receiverName, Authentication authentication) {
        isRequestFromMe(authentication, myName);
        friendService.denyRequest(myName, receiverName);
    }

    private void isRequestFromMe(Authentication authentication, String myName) {
        User user = (User) authentication.getPrincipal();
        if (!user.getUsername().equals(myName))
            throw new RestApiException(UserErrorCode.UNAUTHORIZED);
    }

    private void sendEventOfRequest(String myName, String receiverName, String eventName, Long eventId) {
        try {
            Optional<SseEmittersWithUsername> optEmitter = emitters.getEmitters().stream()
                    .filter(e -> e.getUsername().equals(receiverName))
                    .findAny();
            if (optEmitter.isEmpty()) return;
            SseEmitter emitter = optEmitter.get().getEmitter();
            emitter.send(
                    SseEmitter.event()
                            .id(String.valueOf(eventId))
                            .name(eventName)
                            .data(myName)
            );
        } catch (IOException e) {
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private MessageDto getMessage(Long id, String myName, String receiverName, String friendRequest) {
        return MessageDto.builder()
                .id(id)
                .message(friendRequest)
                .sender(myName)
                .roomId(receiverName)
                .build();
    }
}
