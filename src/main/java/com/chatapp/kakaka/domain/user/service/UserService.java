package com.chatapp.kakaka.domain.user.service;

import com.chatapp.kakaka.config.exception.RestApiException;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.controller.RegisterUserRequest;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.chatapp.kakaka.config.exception.errorcode.UserErrorCode.DUPLICATED_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User register(RegisterUserRequest request) {
        String username = request.getUsername();

        isNotDuplicatedUsername(username);
        User user = createUser(username);

        log.info("user, \"{}\" is created", user.getUsername());
        return userRepository.save(user);
    }

    private User createUser(String username) {
        String pw = UUID.randomUUID().toString();
        User user = User.createUser(username, pw);
        return user;
    }

    private void isNotDuplicatedUsername(String username) {
        Optional<User> optUser = userRepository.findUserByUsername(username);
        if (optUser.isPresent()) throw new RestApiException(DUPLICATED_USERNAME);
    }
}
