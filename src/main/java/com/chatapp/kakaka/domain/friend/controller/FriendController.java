package com.chatapp.kakaka.domain.friend.controller;

import com.chatapp.kakaka.config.sse.SseEmitters;
import com.chatapp.kakaka.config.sse.SseEmittersWithUsername;
import com.chatapp.kakaka.domain.friend.controller.dto.FriendListResponse;
import com.chatapp.kakaka.domain.friend.service.FriendService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.CommonErrorCode;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
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
    private final SseEmitters emitters;

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
    public SseEmitter connectRequest(@RequestParam() String myName) {
        if (myName == null || myName.isBlank())
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);

        SseEmitter sseEmitter = new SseEmitter();
        emitters.add(myName, sseEmitter);
        return sseEmitter;
    }

    @PostMapping("/friend/request/{myName}/{receiverName}")
    public void sendRequest(@PathVariable String myName, @PathVariable String receiverName, Authentication authentication) {
        isRequestFromMe(authentication.getPrincipal(), myName);
        friendService.sendRequest(myName, receiverName);
        sendEventOfRequest(myName, receiverName);
    }

    @PostMapping("/friend/request/accept/{myName}/{receiverName}")
    public void acceptRequest(@PathVariable String myName, @PathVariable String receiverName, Authentication authentication) {
        isRequestFromMe(authentication.getPrincipal(), myName);
        friendService.acceptRequest(myName, receiverName);
    }

    @PostMapping("/friend/request/deny/{myName}/{receiverName}")
    public void denyRequest(@PathVariable String myName, @PathVariable String receiverName, Authentication authentication) {
        isRequestFromMe(authentication.getPrincipal(), myName);
        friendService.denyRequest(myName, receiverName);
    }

    private void isRequestFromMe(Object authentication, String myName) {
        User user = (User) authentication;
        if (!user.getUsername().equals(myName))
            throw new RestApiException(UserErrorCode.UNAUTHORIZED);
    }

    private void sendEventOfRequest(String myName, String receiverName) {
        try {
            Optional<SseEmittersWithUsername> optEmitter = emitters.getEmitters().stream()
                    .filter(e -> e.getUsername().equals(receiverName))
                    .findAny();
            if (optEmitter.isEmpty()) return;
            SseEmitter emitter = optEmitter.get().getEmitter();
            emitter.send(
                    SseEmitter.event()
                        .name("connect")
                        .data(myName)
            );
        } catch (IOException e) {
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
