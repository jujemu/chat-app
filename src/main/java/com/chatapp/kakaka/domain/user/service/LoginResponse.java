package com.chatapp.kakaka.domain.user.service;

import com.chatapp.kakaka.domain.user.User;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final User user;
    private boolean isCreated;

    public LoginResponse(User user) {
        this.user = user;
        this.isCreated = false;
    }

    public LoginResponse(User user, boolean isCreated) {
        this.user = user;
        this.isCreated = isCreated;
    }

}
