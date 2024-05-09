package com.chatapp.kakaka.domain.chat.service;

import com.chatapp.kakaka.domain.chat.ChatRoom;
import com.chatapp.kakaka.domain.chat.repository.ChatRoomRepository;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserRepository userRepository;

    @DisplayName("상대방과의 채팅방을 조회할 수 있다.")
    @Test
    void getChatRoom() throws Exception {
        // given
        User userA = User.createUser("userA", "test uuid");
        User userB = User.createUser("userB", "test uuid");
        userRepository.saveAll(List.of(userA, userB));

        ChatRoom chatRoomCreated = ChatRoom.createChatRoom(userA, userB);
        chatRoomRepository.save(chatRoomCreated);

        // when
        ChatRoom chatRoomFound = chatRoomService.getChatRoom("userA", "userB");

        // then
        assertThat(chatRoomFound).isEqualTo(chatRoomCreated);
        assertThat(chatRoomFound.getUserA().getId())
                .isLessThan(chatRoomFound.getUserB().getId());
    }

    @DisplayName("상대방과의 채팅방을 조회할 수 있다. 만약 기존에 채팅방이 없다면 새로 생성한다.")
    @Test
    void getChatRoom_first_access() throws Exception {
        // given
        User userA = User.createUser("userA", "test uuid");
        User userB = User.createUser("userB", "test uuid");
        userRepository.saveAll(List.of(userA, userB));

        List<ChatRoom> chatRoomAll = chatRoomRepository.findAll();

        // when
        ChatRoom chatRoomFound = chatRoomService.getChatRoom("userA", "userB");

        // then
        assertThat(chatRoomAll).isEmpty();
        assertThat(chatRoomFound.getId()).isNotNull();
        assertThat(chatRoomFound.getUserA().getId())
                .isLessThan(chatRoomFound.getUserB().getId());
    }
}