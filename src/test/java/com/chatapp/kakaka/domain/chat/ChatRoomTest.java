package com.chatapp.kakaka.domain.chat;

import com.chatapp.kakaka.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatRoomTest extends User {

    @DisplayName("1:1 대화방을 생성할 수 있다. userA 와 userB 는 id 순으로 결정된다.")
    @Test
    void createChatRoom() throws Exception {
        // given
        User userA = User.createWithId(1L, "userA");
        User userB = User.createWithId(2L, "userB");

        // when
        ChatRoom chatRoom1 = ChatRoom.createChatRoom(userA, userB);
        ChatRoom chatRoom2 = ChatRoom.createChatRoom(userB, userA);

        // then
        assertThat(chatRoom1)
                .extracting("userA", "userB")
                .containsExactly(userA, userB);

        assertThat(chatRoom2)
                .extracting("userA", "userB")
                .containsExactly(userA, userB);
    }

    @DisplayName("나 자신과의 채팅방을 만들 수 없다.")
    @Test
    void cannot_createChatRoom_with_me() throws Exception {
        // given
        User user = User.createUser("userA", "test uuid");

        // when // then
        assertThatThrownBy(() ->
                ChatRoom.createChatRoom(user, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("UserA and userB can not be null or same.");
    }
}