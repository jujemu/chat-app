package com.chatapp.kakaka.domain.user.service;

import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.controller.RegisterUserRequest;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public String register(RegisterUserRequest request) {
        String username = request.getUsername();
        isNotDuplicatedUsername(username);
        String pw = UUID.randomUUID().toString();
        User user = User.createUser(username, pw);
        return userRepository.save(user).getPw();
    }

    private void isNotDuplicatedUsername(String username) {
        Optional<User> optUser = userRepository.findUserByUsername(username);
        if (optUser.isPresent()) throw new IllegalArgumentException("중복된 회원 이름입니다.");
    }
}
