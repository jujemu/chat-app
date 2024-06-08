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
import org.springframework.security.core.userdetails.UserDetails;

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
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(columnDefinition = "bigint DEFAULT 0")
    private Long lastEventId = 0L;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static User createUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .role(UserRole.USER)
                .build();
    }

    public static User createPlusUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .role(UserRole.PLUS)
                .build();
    }

    public UserDetails getUserDetails() {
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .authorities(List.of(() ->
                        String.valueOf(role))
                )
                .build();
    }

    public void updateEvent(Long lastEventId) {
        this.lastEventId = lastEventId;
    }

    /**
     * only for test
     */
    private User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    protected static User createWithId(Long id, String username) {
        return new User(id, username);
    }
}
