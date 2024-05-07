package com.chatapp.kakaka.domain.chat.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomGetRequest {

    @NotBlank(message = "유저 이름은 필수값입니다.")
    private String username;

    public ChatRoomGetRequest(String username) {
        this.username = username;
    }
}
