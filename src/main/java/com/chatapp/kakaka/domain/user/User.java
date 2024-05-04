package com.chatapp.kakaka.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    private String pw;

    @Builder
    private User(String username, String pw) {
        this.username = username;
        this.pw = pw;
    }

    public static User createUser(String username, String pw) {
        return User.builder()
                .username(username)
                .pw(pw)
                .build();
    }
}
