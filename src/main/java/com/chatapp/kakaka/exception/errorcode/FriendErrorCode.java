package com.chatapp.kakaka.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum FriendErrorCode implements ErrorCode{

    DUPLICATED_REQUEST(BAD_REQUEST, "Friend request already exists."),
    REQUEST_NOT_EXISTS(BAD_REQUEST, "The request doesn't exist.");

    private final HttpStatus httpStatus;
    private final String message;
}
