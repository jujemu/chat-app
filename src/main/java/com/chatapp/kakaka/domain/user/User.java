package com.chatapp.kakaka.domain.user;

import com.chatapp.kakaka.domain.BaseEntity;
import com.chatapp.kakaka.domain.chat.ChatRoom;
import com.chatapp.kakaka.domain.friend.Friend;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
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
    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;
    private String uuid;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "userA")
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Friend> myFriends;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String username, String uuid, UserRole role) {
        this.username = username;
        this.uuid = uuid;
        this.role = role;
    }

    public static User createUser(String username, String uuid) {
        return User.builder()
                .username(username)
                .uuid(uuid)
                .role(UserRole.USER)
                .build();
    }

    public static User createPlusUser(String username, String uuid) {
        return User.builder()
                .username(username)
                .uuid(uuid)
                .role(UserRole.PLUS)
                .build();
    }

    /**
     * only for test
     */
    protected static User createWithId(Long id, String username) {
        return new User(id, username);
    }

    private User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
