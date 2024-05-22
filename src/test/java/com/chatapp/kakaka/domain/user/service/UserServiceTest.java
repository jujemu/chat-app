package com.chatapp.kakaka.domain.user.service;

import com.chatapp.kakaka.domain.friend.Friend;
import com.chatapp.kakaka.domain.friend.FriendStatus;
import com.chatapp.kakaka.domain.friend.repository.FriendRepository;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import com.chatapp.kakaka.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @DisplayName("username 으로 유저를 생성한다.")
    @Test
    void register() throws Exception {
        // given
        String username = "test";
        String password = "test password";

        // when
        userService.registerUser(username, password);

        // then
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("테스트 오류"));
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isNotNull();
    }

    @DisplayName("회원가입을 하면 초기 플러스 친구가 자동으로 추가된다.")
    @Test
    void registerWithPlusFriend() throws Exception {
        // given
        String username = "test";
        String password = "test password";

        User plus1 = createPlusUser("plus1");
        User plus2 = createPlusUser("plus2");
        User plus3 = createPlusUser("plus3");
        userRepository.saveAll(
                List.of(plus1, plus2, plus3)
        );

        // when
        userService.registerUser(username, password);

        // then
        List<Friend> plusFriends = friendRepository.findAllByUserAndStatus(username, FriendStatus.ACCEPTED);
        assertThat(plusFriends).hasSize(3);
        plusFriends.forEach(
                friend ->
                        assertThat(friend.getId()).isNotNull()
        );
    }

    @DisplayName("중복된 username 으로 가입할 수 없다.")
    @Test
    void cannot_register_when_username_duplicated() throws Exception {
        // given
        String username = "test";
        String password = "test password";
        User originalUser = User.createUser(username, password);
        userRepository.save(originalUser);

        // when // then
        assertThatThrownBy(() -> userService.registerUser(username, password))
                .isInstanceOf(RestApiException.class)
                .satisfies(e ->
                        assertThat(((RestApiException) e).getErrorCode().getMessage())
                                .isEqualTo("The username already exists"));
    }

    private User createPlusUser(String username) {
        return User.createPlusUser(username, "test uuid");
    }
}