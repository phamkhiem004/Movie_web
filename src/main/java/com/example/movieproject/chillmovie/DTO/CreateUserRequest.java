package com.example.movieproject.chillmovie.DTO;

import java.time.Instant;

public class CreateUserRequest {
    public String username;
    public String email;
    public String password;
    public String fullName;
    public Instant createdAt;
}
