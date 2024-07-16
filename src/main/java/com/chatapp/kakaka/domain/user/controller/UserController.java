package com.chatapp.kakaka.domain.user.controller;

import com.chatapp.kakaka.config.sse.SseEmitters;
import com.chatapp.kakaka.config.sse.SseEmittersWithUsername;
import com.chatapp.kakaka.domain.friend.event.FriendRequestEvent;
import com.chatapp.kakaka.domain.user.controller.dto.RegisterUserRequest;
import com.chatapp.kakaka.domain.user.service.UserService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.CommonErrorCode;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final SseEmitters emitters;
    private final Environment environment;

    @GetMapping("/create/available")
    public boolean check(@RequestParam String username) {
        return userService.canCreate(username);
    }

    @PostMapping("/create")
    public void create(@Valid @RequestBody RegisterUserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        userService.registerUser(username, password);
    }

    /*
        index.html 로그인 폼을 사용하여 로그인한다.
        기존에 없던 아이디로 접근하면 자동으로 회원가입을 진행한다.
        이미 등록되어있는 아이디로 접근하면 비밀번호를 대조한다.
     */
    @GetMapping("/login")
    public void login() {}

    @GetMapping("/events")
    public void getLastEvent(@RequestParam String myName, @RequestParam Long lastEventId, Authentication authentication) {
        isRequestFromMe(authentication, myName);
        isConnected(myName);
        List<FriendRequestEvent> events = userService.getLastEventId(myName, lastEventId);
        if (events == null || events.isEmpty()) return;
        events.forEach(this::sendEventOfRequest);
    }

    private void isRequestFromMe(Authentication authentication, String myName) {
        User user = (User) authentication.getPrincipal();
        if (!user.getUsername().equals(myName))
            throw new RestApiException(UserErrorCode.UNAUTHORIZED);
    }

    private void isConnected(String myName) {
        if (emitters.getEmitters().stream().noneMatch(emitter -> emitter.getUsername().equals(myName)))
            throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
    }

    private void sendEventOfRequest(FriendRequestEvent event) {
        try {
            Optional<SseEmittersWithUsername> optEmitter = emitters.getEmitters().stream()
                    .filter(e -> e.getUsername().equals(event.getReceiverName()))
                    .findAny();
            if (optEmitter.isEmpty()) return;
            SseEmitter emitter = optEmitter.get().getEmitter();
            emitter.send(
                    SseEmitter.event()
                            .id(String.valueOf(event.getId()))
                            .name("friendRequest")
                            .data(event.getSenderName())
            );
        } catch (IOException e) {
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
