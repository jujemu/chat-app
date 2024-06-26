package com.chatapp.kakaka.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{

    DUPLICATED_USERNAME(HttpStatus.FORBIDDEN, "The username already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "The user cannot be found."),
    PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "Password is incorrect."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized request.");

    private final HttpStatus httpStatus;
    private final String message;
}
