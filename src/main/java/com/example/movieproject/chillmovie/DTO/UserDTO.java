package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.UserStatus;
import com.example.movieproject.chillmovie.util.EnumPattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull(message = "Username can't be null")
    private String username;

    @NotNull(message = "Email can't be null")
    private String email;
    private String role;

    @EnumPattern(name ="status", regexp = "ACTIVE|BLOCKED")
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    @NotNull(message = "Fullname can't be null")
    private String fullName;

}
