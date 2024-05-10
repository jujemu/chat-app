package com.chatapp.kakaka.domain.friend.repository;

import com.chatapp.kakaka.domain.friend.Friend;
import com.chatapp.kakaka.domain.friend.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from Friend f join fetch f.sender s where s.username =:sender and f.status =:status")
    List<Friend> findAllByUserAndStatus(String sender, FriendStatus status);

    @Query("select f from Friend f join f.sender s join f.receiver r where s.username =:senderName and r.username =:receiverName")
    Optional<Friend> findBySenderAndReceiver(String senderName, String receiverName);
}
