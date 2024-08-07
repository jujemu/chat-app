package com.chatapp.kakaka.config.redis.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PubSubMessage {
    FRIEND_REQUEST("친구를 요청하는 알림", "friendRequest"),
    REQUEST_ACCEPT("친구 요청을 수락하는 알림", "requestAccept");

    private final String detail;
    private final String text;
}
