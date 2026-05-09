package com.example.movieproject.chillmovie.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
public class CreateUserRequest {
    @NotBlank(message = "Username can't be null")
    public String username;

    @Email(message = "Email not valid")
    @NotBlank(message = "Email can't be null")
    public String email;

    @NotBlank(message = "Password can't be null")
    public String password;

    @NotBlank(message = "Full name can't be null")
    public String fullName;

    public Instant createdAt;
}
