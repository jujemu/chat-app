package com.chatapp.kakaka.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum FriendErrorCode implements ErrorCode{

    DUPLICATED_REQUEST(BAD_REQUEST, "Friend request already exists.");

    private final HttpStatus httpStatus;
    private final String message;
}
