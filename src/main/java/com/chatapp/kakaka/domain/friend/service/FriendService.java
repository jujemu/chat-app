package com.chatapp.kakaka.domain.friend.service;

import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.FriendErrorCode;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import com.chatapp.kakaka.domain.friend.Friend;
import com.chatapp.kakaka.domain.friend.FriendStatus;
import com.chatapp.kakaka.domain.friend.controller.dto.FriendListResponse;
import com.chatapp.kakaka.domain.friend.repository.FriendRepository;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public FriendListResponse showAll(String myName) {
        User me = getUser(myName);
        List<Friend> myFriends = friendRepository.findAllBySender(me);

        return FriendListResponse.of(me.getId(), myFriends);
    }

    /*
    친구를 요청할 때는 sender(me) 와 receiver 값을 번갈아가며, 2개의 튜플로 저장한다.
     */
    public void sendRequest(String myName, String receiverName) {
        User sender = getUser(myName);
        User receiver = getUser(receiverName);

        cannotDuplicatedRequest(sender, receiver);

        List<Friend> friends = Friend.requestFriend(sender, receiver);
        friendRepository.saveAll(friends);
    }

    private void cannotDuplicatedRequest(User sender, User receiver) {
        Optional<Friend> optFriend = friendRepository.findBySenderAndReceiver(sender, receiver);
        if (optFriend.isPresent() && optFriend.get().getStatus() != FriendStatus.DENIED) {
            throw new RestApiException(FriendErrorCode.DUPLICATED_REQUEST);
        }
    }

    private User getUser(String myName) {
        return userRepository.findUserByUsername(myName)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }
}
