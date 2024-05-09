package com.chatapp.kakaka.domain.chat.service;

import com.chatapp.kakaka.exception.RestApiException;
import com.chatapp.kakaka.exception.errorcode.CommonErrorCode;
import com.chatapp.kakaka.exception.errorcode.UserErrorCode;
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

    public ChatRoom getChatRoom(String senderName, String receiverName) {
        if (senderName == null || receiverName == null || senderName.equals(receiverName))
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
        User userA = getUser(senderName);
        User userB = getUser(receiverName);

        User lowerUser, upperUser;
        if (userA.getId() < userB.getId()) {
            lowerUser = userA;
            upperUser = userB;
        } else {
            lowerUser = userB;
            upperUser = userA;
        }
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByUserAAndUserB(lowerUser, upperUser)
                .orElseGet(() -> ChatRoom.createChatRoom(lowerUser, upperUser));

        return chatRoomRepository.save(chatRoom);
    }

    private User getUser(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

}
