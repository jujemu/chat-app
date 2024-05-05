package com.chatapp.kakaka.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String uuid;

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
