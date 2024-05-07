package com.chatapp.kakaka.domain.chat.repository;

import com.chatapp.kakaka.domain.chat.ChatRoom;
import com.chatapp.kakaka.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findChatRoomByUserAAndUserB(User userA, User userB);
}
