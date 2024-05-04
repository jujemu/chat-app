package com.chatapp.kakaka.domain.user.controller;

import com.chatapp.kakaka.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public String registerNewUser(@Valid @RequestBody RegisterUserRequest request) {
        return userService.register(request);
    }
}
