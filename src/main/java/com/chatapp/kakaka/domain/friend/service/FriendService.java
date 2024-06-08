package com.chatapp.kakaka.domain.friend.service;

import com.chatapp.kakaka.domain.friend.event.service.EventService;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.CommonErrorCode;
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

import static com.chatapp.kakaka.exception.errorcode.FriendErrorCode.REQUEST_NOT_EXISTS;

@RequiredArgsConstructor
@Transactional
@Service
public class FriendService {

    private final EventService eventService;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public FriendListResponse showAll(String myName) {
        List<Friend> myFriends = friendRepository.findAllByReceiverAndStatus(myName, FriendStatus.ACCEPTED);
        return FriendListResponse.of(myName, myFriends);
    }

    @Transactional(readOnly = true)
    public FriendListResponse showRequests(String myName) {
        List<Friend> waitingRequests = friendRepository.findAllByReceiverAndStatus(myName, FriendStatus.WAITING);
        return FriendListResponse.of(myName, waitingRequests);
    }

    /*
    친구를 요청할 때는 sender(me) 와 receiver 값을 번갈아가며, 2개의 튜플로 저장한다.
     */
    public Long sendRequest(String myName, String receiverName) {
        User sender = getUser(myName);
        User receiver = getUser(receiverName);

        Optional<Friend> optFriend = getOptFriend(myName, receiverName, sender, receiver);

        if (isRequestDenied(optFriend))
            return handleAlreadyRequest(myName, receiverName, optFriend.get());

        return handleNewRequest(sender, receiver);
    }

    public Long acceptRequest(String myName, String receiverName) {
        List<Friend> friendRequests = getFriendRequests(myName, receiverName);
        friendRequests.forEach(Friend::accepted);
        Friend friend = friendRequests.get(0);
        return eventService.create(friend.getSender().getUsername(), friend.getReceiver().getUsername(), "requestAccept");
    }

    public void denyRequest(String myName, String receiverName) {
        List<Friend> friendRequests = getFriendRequests(myName, receiverName);
        friendRequests.forEach(Friend::denied);
    }

    private User getUser(String myName) {
        return userRepository.findUserByUsername(myName)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    private void cannotDuplicatedRequest(Optional<Friend> optFriend) {
        if (optFriend.isPresent() && optFriend.get().getStatus() != FriendStatus.DENIED) {
            throw new RestApiException(FriendErrorCode.DUPLICATED_REQUEST);
        }
    }

    private Friend getFriendRequested(String myName, String receiverName) {
        return friendRepository.findBySenderAndReceiver(myName, receiverName)
                .orElseThrow(() -> new RestApiException(REQUEST_NOT_EXISTS));
    }

    private void cannotRequestMyself(String myName, String receiverName) {
        if (myName == null || receiverName == null || myName.equals(receiverName))
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
    }

    private List<Friend> getFriendRequests(String myName, String receiverName) {
        List<Friend> friendRequests = List.of(
                getFriendRequested(myName, receiverName),
                getFriendRequested(receiverName, myName)
        );
        if (friendRequests.stream().anyMatch(f ->
                f.getStatus() != FriendStatus.WAITING && f.getStatus() != FriendStatus.REQUESTED))
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        return friendRequests;
    }

    private void cannotRequestFriend(Optional<Friend> optFriend) {
        if (optFriend.isPresent() && optFriend.get().getStatus().equals(FriendStatus.ACCEPTED))
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
    }

    private boolean isRequestDenied(Optional<Friend> optFriend) {
        return optFriend.isPresent() && optFriend.get().getStatus().equals(FriendStatus.DENIED);
    }

    private Optional<Friend> getOptFriend(String myName, String receiverName, User sender, User receiver) {
        Optional<Friend> optFriend = friendRepository.findBySenderAndReceiver(myName, receiverName);
        cannotRequestMyself(sender.getUsername(), receiver.getUsername());
        cannotDuplicatedRequest(optFriend);
        cannotRequestFriend(optFriend);
        return optFriend;
    }

    private Long handleAlreadyRequest(String myName, String receiverName, Friend friend) {
        Friend friendReverse = friendRepository.findBySenderAndReceiver(receiverName, myName).orElseThrow();
        friend.reRequestedSender();
        friendReverse.reRequestedReceiver();
        Long eventId = eventService.create(friend.getSender().getUsername(), friend.getReceiver().getUsername(), "friendRequest");
        friend.getReceiver().updateEvent(eventId);
        return eventId;
    }

    private Long handleNewRequest(User sender, User receiver) {
        List<Friend> friends = Friend.requestFriend(sender, receiver);
        friendRepository.saveAll(friends);
        Friend friend = friends.get(0);
        Long eventId = eventService.create(friend.getSender().getUsername(), friend.getReceiver().getUsername(), "friendRequest");
        friend.getReceiver().updateEvent(eventId);
        return eventId;
    }
}
