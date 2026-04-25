package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private Long role;
    private UserStatus status;
    private Instant createdAt;
    private String fullname;

}
