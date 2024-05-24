package com.chatapp.kakaka.domain.friend;

import com.chatapp.kakaka.domain.BaseEntity;
import com.chatapp.kakaka.domain.user.User;
import com.chatapp.kakaka.domain.user.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Friend extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FriendStatus status;
    @Enumerated(EnumType.STRING)
    private FriendType type;

    @JoinColumn(name = "sender_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @JoinColumn(name = "receiver_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @Builder(access = AccessLevel.PRIVATE)
    private Friend(User sender, User receiver, FriendStatus status, FriendType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.type = type;
    }

    /*
    친구 요청을 보내면 상대방도 요청을 보낸 것으로 처리한다.
    결과적으로 친구 관계를 저장하기 위해서 2개의 튜플을 사용한다.
     */
    public static List<Friend> requestFriend(User me, User friend) {
        if (me == null || friend == null || me.equals(friend))
            throw new IllegalArgumentException("Me and my friend can not be null or same.");

        Friend friend1 = Friend.builder()
                .status(FriendStatus.WAITING)
                .type(FriendType.NORMAL)
                .sender(me)
                .receiver(friend)
                .build();

        Friend friend2 = Friend.builder()
                .status(FriendStatus.REQUESTED)
                .type(FriendType.NORMAL)
                .sender(friend)
                .receiver(me)
                .build();

        return List.of(friend1, friend2);
    }

    public static Friend requestPlusFriend(User me, User plus) {
        if (me == null || plus == null || me.equals(plus))
            throw new IllegalArgumentException("Me and my friend can not be null or same.");
        if (plus.getRole() != UserRole.PLUS)
            throw new IllegalArgumentException("Only for plus user");

        return Friend.builder()
                .status(FriendStatus.ACCEPTED)
                .type(FriendType.PLUS)
                .sender(plus)
                .receiver(me)
                .build();
    }

    public void accepted() {
        if (this.status != FriendStatus.WAITING && this.status != FriendStatus.REQUESTED)
            throw new IllegalArgumentException("This request is already processed.");
        this.status = FriendStatus.ACCEPTED;
    }

    public void denied() {
        if (this.status != FriendStatus.WAITING && this.status != FriendStatus.REQUESTED)
            throw new IllegalArgumentException("This request is already processed.");
        this.status = FriendStatus.DENIED;
    }
}
