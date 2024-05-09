package com.chatapp.kakaka.domain.user;

import com.chatapp.kakaka.domain.BaseEntity;
import com.chatapp.kakaka.domain.chat.ChatRoom;
import com.chatapp.kakaka.domain.friend.Friend;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String uuid;

    @OneToMany(mappedBy = "userA")
    private List<ChatRoom> chatRooms;

    @OneToOne(mappedBy = "sender", fetch = FetchType.LAZY)
    private Friend me;
    @OneToOne(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Friend you;

    @Builder
    private User(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
    }

    public static User createUser(String username, String uuid) {
        return User.builder()
                .username(username)
                .uuid(uuid)
                .build();
    }
}
