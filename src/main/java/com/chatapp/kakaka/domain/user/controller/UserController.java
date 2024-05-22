package com.chatapp.kakaka.domain.user.controller;

import com.chatapp.kakaka.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/create/available")
    public boolean check(@RequestParam String username) {
        return userService.canCreate(username);
    }

    @PostMapping("/create")
    public void create(@Valid @RequestBody RegisterUserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        userService.registerUser(username, password);
    }

    @GetMapping("/login")
    public void login() {}
}
