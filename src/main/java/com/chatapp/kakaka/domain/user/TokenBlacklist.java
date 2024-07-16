package com.chatapp.kakaka.domain.user;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "token-blacklist", timeToLive = 2 * 60 * 60)
public class TokenBlacklist {

    @Id
    private String token;
    private String username;
}
