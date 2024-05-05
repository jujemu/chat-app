package com.chatapp.kakaka.domain.user.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "유저 이름은 필수값입니다.")
    private String username;
}
