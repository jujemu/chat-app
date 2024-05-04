package com.chatapp.kakaka.config.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{

    DUPLICATED_USERNAME(FORBIDDEN, "The username already exists");

    private final HttpStatus httpStatus;
    private final String message;
}
