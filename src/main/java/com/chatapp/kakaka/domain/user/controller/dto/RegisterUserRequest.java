package com.chatapp.kakaka.domain.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "유저 이름은 필수값입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;

    public RegisterUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
