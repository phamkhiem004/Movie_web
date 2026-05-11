package com.example.movieproject.chillmovie.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("RedisToken")
public class RedisToken implements Serializable {


    @Id
    private String id;
    private String accessToken;
    private String refreshToken;
    private String resetToken;

}
