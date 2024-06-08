package com.chatapp.kakaka.domain.user.service;

import com.chatapp.kakaka.domain.friend.Friend;
import com.chatapp.kakaka.domain.friend.event.FriendRequestEvent;
import com.chatapp.kakaka.domain.friend.event.repository.EventRepository;
import com.chatapp.kakaka.domain.friend.repository.FriendRepository;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final EventRepository eventRepository;

    public boolean canCreate(String username) {
        return userRepository.findUserByUsername(username).isEmpty();
    }

    public void registerUser(String username, String password) {
        if (userRepository.findUserByUsername(username).isPresent())
            throw new RestApiException(UserErrorCode.DUPLICATED_USERNAME);
        User user = User.createUser(username, password);
        userRepository.save(user);

        // 회원가입을 하면 자동적으로 5명의 플러스 친구를 갖는다.
        List<User> plusUsers = userRepository.findPlusUsers();
        List<Friend> initFriends = plusUsers.stream()
                .map(plus ->
                        Friend.requestPlusFriend(user, plus))
                .toList();
        friendRepository.saveAll(initFriends);
    }

    @Transactional(readOnly = true)
    public List<FriendRequestEvent> getLastEventId(String myName, Long lastEventId) {
        User user = userRepository.findUserByUsername(myName).orElseThrow(() ->
                new UsernameNotFoundException("The user with given username does not exist."));
        if (isOutUpdated(lastEventId, user))
            return eventRepository.findByReceiverNameAndIdGreaterThan(myName, lastEventId);
        return null;
    }

    private boolean isOutUpdated(Long lastEventId, User user) {
        return !lastEventId.equals(user.getLastEventId());
    }
}
