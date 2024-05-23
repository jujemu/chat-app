package com.chatapp.kakaka.domain.friend.controller;

import com.chatapp.kakaka.domain.friend.controller.dto.FriendListResponse;
import com.chatapp.kakaka.domain.friend.service.FriendService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friend/all/{myName}")
    public FriendListResponse showAll(@PathVariable String myName) {
        return friendService.showAll(myName);
    }

    @GetMapping("/friend/request/all/{myName}")
    public FriendListResponse showRequests(@PathVariable String myName) {
        return friendService.showRequests(myName);
    }

    @PostMapping("/friend/request/{myName}/{receiverName}")
    public void sendRequest(@PathVariable String myName, @PathVariable String receiverName, Authentication authentication) {
        isRequestFromMe(authentication.getPrincipal(), myName);
        friendService.sendRequest(myName, receiverName);
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
}
