package com.example.movieproject.chillmovie.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "RedisToken", timeToLive = 3600)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisToken implements Serializable {
    private String id;
    private String accessToken;
    private String refreshToken;
    private String resetToken;
}
