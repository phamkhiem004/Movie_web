package com.example.movieproject.chillmovie.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;

}
