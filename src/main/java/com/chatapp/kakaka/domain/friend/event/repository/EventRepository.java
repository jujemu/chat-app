package com.chatapp.kakaka.domain.friend.event.repository;

import com.chatapp.kakaka.domain.friend.event.FriendRequestEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<FriendRequestEvent, Long> {

    List<FriendRequestEvent> findByReceiverNameAndIdGreaterThan(String receiverName, Long id);
}
