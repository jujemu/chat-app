package com.chatapp.kakaka.domain.friend;

import com.chatapp.kakaka.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.tuple;

class FriendTest {

    @DisplayName("친구 요청을 보내면 WAITING 상태를 가진다.")
    @Test
    void requestFriend() throws Exception {
        // given
        User userA = User.createUser("userA", "test uuid");
        User userB = User.createUser("userB", "test uuid");

        // when
        List<Friend> friends = Friend.requestFriend(userA, userB);

        // then
        assertThat(friends).hasSize(2)
                .extracting("sender", "receiver", "status", "type")
                .containsExactly(
                        tuple(userA, userB, FriendStatus.WAITING, FriendType.NORMAL),
                        tuple(userB, userA, FriendStatus.WAITING, FriendType.NORMAL)
                );
    }

    @DisplayName("나 자신과 친구 요청을 할 수 없다.")
    @Test
    void cannot_requestFriend_with_me() throws Exception {
        // given
        User user = User.createUser("userA", "test uuid");

        // when // then
        assertThatThrownBy(() ->
                        Friend.requestFriend(user, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Me and my friend can not be null or same.");
    }

    @DisplayName("플러스 유저에게 친구 요청을 보낼 수 있다.")
    @Test
    void requestPlusFriend() throws Exception {
        // given
        User user = User.createUser("user", "test uuid");
        User plus = User.createPlusUser("plus", "test uuid");

        // when
        Friend plusFriend = Friend.requestPlusFriend(user, plus);

        // then
        assertThat(plusFriend.getStatus()).isEqualTo(FriendStatus.ACCEPTED);
        assertThat(plusFriend.getType()).isEqualTo(FriendType.PLUS);
        assertThat(plusFriend.getSender()).isEqualTo(user);
        assertThat(plusFriend.getReceiver()).isEqualTo(plus);
    }

    @DisplayName("일반 유저에게 플러스 친구 요청을 보낼 수 없다.")
    @Test
    void requestPlusFriend_with_normal_user() throws Exception {
        // given
        User user = User.createUser("user", "test uuid");
        User normal = User.createUser("normal", "test uuid");

        // when // then
        assertThatThrownBy(() ->
                Friend.requestPlusFriend(user, normal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only for plus user");
    }
}