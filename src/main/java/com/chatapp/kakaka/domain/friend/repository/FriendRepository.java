package com.chatapp.kakaka.domain.friend.repository;

import com.chatapp.kakaka.domain.friend.Friend;
import com.chatapp.kakaka.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findAllBySender(User sender);
    Optional<Friend> findBySenderAndReceiver(User sender, User receiver);
}
