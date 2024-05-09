package com.chatapp.kakaka.domain.friend.service;

import com.chatapp.kakaka.domain.friend.Friend;
import com.chatapp.kakaka.domain.friend.FriendStatus;
import com.chatapp.kakaka.domain.friend.controller.dto.FriendListResponse;
import com.chatapp.kakaka.domain.friend.repository.FriendRepository;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class FriendServiceTest {

    @Autowired
    FriendService friendService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @DisplayName("내 친구 목록을 조회한다.")
    @Test
    void showAll() throws Exception {
        // given
        String myName = "test";
        User user = User.createUser(myName, "test uuid");
        User friend1 = User.createPlusUser("friend1", "test uuid");
        User friend2 = User.createPlusUser("friend2", "test uuid");
        User friend3 = User.createPlusUser("friend3", "test uuid");
        userRepository.saveAll(List.of(user, friend1, friend2, friend3));

        List<Friend> friends = List.of(
                Friend.requestPlusFriend(user, friend1),
                Friend.requestPlusFriend(user, friend2),
                Friend.requestPlusFriend(user, friend3)
        );
        friendRepository.saveAll(friends);

        // when
        FriendListResponse response = friendService.showAll(myName);

        // then
        assertThat(response.getMyId()).isNotNull();
        assertThat(response.getFriends()).hasSize(3)
                .extracting("yours")
                .containsExactly("friend1", "friend2", "friend3");
        response.getFriends().forEach(f ->
                assertThat(f.getId()).isNotNull()
        );
    }

    @DisplayName("친구를 추가할 수 있다. 내가 요청한 것과 상대방이 요청한 것, 2개의 Friend 가 생성된다.")
    @Test
    void sendRequest() throws Exception {
        // given
        String myName = "test";
        String friendName = "friend";
        User me = User.createUser(myName, "test uuid");
        User friend = User.createUser(friendName, "test uuid");
        userRepository.saveAll(List.of(me, friend));

        // when
        friendService.sendRequest(myName, friendName);

        // then
        Optional<Friend> friendByMe = friendRepository.findBySenderAndReceiver(me, friend);
        assertThat(friendByMe.isPresent()).isTrue();
        assertThat(friendByMe.get().getId()).isNotNull();
        assertThat(friendByMe.get().getStatus()).isEqualTo(FriendStatus.WAITING);

        Optional<Friend> friendByFriend = friendRepository.findBySenderAndReceiver(friend, me);
        assertThat(friendByFriend.isPresent()).isTrue();
        assertThat(friendByFriend.get().getId()).isNotNull();
        assertThat(friendByFriend.get().getStatus()).isEqualTo(FriendStatus.WAITING);
    }

}