package com.chatapp.kakaka.domain.user.service;

import com.chatapp.kakaka.config.exception.RestApiException;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.controller.RegisterUserRequest;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @DisplayName("username 으로 유저를 생성한다.")
    @Test
    void register() throws Exception {
        // given
        String username = "test";
        RegisterUserRequest request = new RegisterUserRequest(username);

        // when
        User user = userService.register(request);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getUuid()).isNotNull();
    }

    @DisplayName("중복된 username 으로 가입할 수 없다.")
    @Test
    void cannot_register_when_username_duplicated() throws Exception {
        // given
        String username = "test";
        User originalUser = createUser(username);
        userRepository.save(originalUser);
        RegisterUserRequest request = new RegisterUserRequest(username);

        // when // then
        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(RestApiException.class)
                .satisfies(e ->
                        assertThat(((RestApiException) e).getErrorCode().getMessage())
                                .isEqualTo("The username already exists"));
    }

    private User createUser(String username) {
        return User.builder()
                .username(username)
                .uuid("test uuid")
                .build();
    }
}