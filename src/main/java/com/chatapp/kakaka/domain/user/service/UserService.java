package com.chatapp.kakaka.domain.user.service;

import com.chatapp.kakaka.config.exception.RestApiException;
import com.chatapp.kakaka.config.exception.errorcode.UserErrorCode;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.controller.RegisterUserRequest;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    /*
    만약 등록되어 있지 않은 회원이라면 username 으로 회원가입을 하고,
    등록되어 있는 회원이라면 db에 저장되어 있는 uuid 와 비교한다.
     */
    public LoginResponse login(RegisterUserRequest request) {
        String username = request.getUsername();
        Optional<User> optUser = userRepository.findUserByUsername(username);
        if (optUser.isPresent()) return loginWithRegisteredUser(request, optUser.get());

        User user = createUser(username);
        userRepository.save(user);
        return new LoginResponse(user, true);
    }

    private LoginResponse loginWithRegisteredUser(RegisterUserRequest request, User user) {
        String uuid = request.getUuid();
        if (uuid == null) throw new RestApiException(UserErrorCode.DUPLICATED_USERNAME);
        if (user.getUuid().equals(uuid)) return new LoginResponse(user);
        throw new RestApiException(UserErrorCode.PASSWORD_INCORRECT);
    }

    private User createUser(String username) {
        String pw = UUID.randomUUID().toString();
        return User.createUser(username, pw);
    }

}
