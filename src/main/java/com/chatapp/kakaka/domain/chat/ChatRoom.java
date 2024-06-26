package com.chatapp.kakaka.domain.chat;

import com.chatapp.kakaka.domain.BaseEntity;
import com.chatapp.kakaka.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_a_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userA;

    @JoinColumn(name = "user_b_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userB;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatRoom(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
    }

    /**
     * id가 작은 유저를 userA에 저장한다.
     */
    public static ChatRoom createChatRoom(User userA, User userB) {
        if (userA == null || userB == null || userA.equals(userB))
            throw new IllegalArgumentException("UserA and userB can not be null or same.");

        if (isFirstIdLower(userA, userB)) {
            return ChatRoom.builder()
                    .userA(userA)
                    .userB(userB)
                    .build();
        }
        return ChatRoom.builder()
                .userA(userB)
                .userB(userA)
                .build();
    }

    private static boolean isFirstIdLower(User userA, User userB) {
        return userA.getId() < userB.getId();
    }

}
