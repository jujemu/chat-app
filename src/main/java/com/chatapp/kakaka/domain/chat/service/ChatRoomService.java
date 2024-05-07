package com.chatapp.kakaka.domain.chat.service;

import com.chatapp.kakaka.config.exception.RestApiException;
import com.chatapp.kakaka.config.exception.errorcode.UserErrorCode;
import com.chatapp.kakaka.domain.chat.ChatRoom;
import com.chatapp.kakaka.domain.chat.repository.ChatRoomRepository;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoom getChatRoom(Long senderId, Long receiverId) {
        User lowerUser = getUser(Math.min(senderId, receiverId));
        User upperUser = getUser(Math.max(senderId, receiverId));
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByUserAAndUserB(lowerUser, upperUser)
                .orElseGet(() -> ChatRoom.createChatRoom(lowerUser, upperUser));

        return chatRoomRepository.save(chatRoom);
    }

    private User getUser(Long userAId) {
        return userRepository.findById(userAId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

}
