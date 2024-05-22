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

    /*
        index.html 로그인 폼을 사용하여 로그인한다.
        기존에 없던 아이디로 접근하면 자동으로 회원가입을 진행한다.
        이미 등록되어있는 아이디로 접근하면 비밀번호를 대조한다.
     */
    @GetMapping("/login")
    public void login() {}
}
