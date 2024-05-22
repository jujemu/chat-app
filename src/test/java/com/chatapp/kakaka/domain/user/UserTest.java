package com.chatapp.kakaka.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("일반 유저를 생성할 수 있다.")
    @Test
    void createUser() throws Exception {
        // given
        String username = "test username";
        String uuid = "test uuid";

        // when
        User user = User.createUser(username, uuid);

        // then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(uuid);
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }

    @DisplayName("플러스친구 유저를 생성할 수 있다.")
    @Test
    void createPlusUser() throws Exception {
        // given
        String username = "test username";
        String uuid = "test uuid";

        // when
        User user = User.createPlusUser(username, uuid);

        // then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(uuid);
        assertThat(user.getRole()).isEqualTo(UserRole.PLUS);
    }
}