package com.example.movieproject.chillmovie.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateActorRequest {
    private String name;
    private LocalDate birthDate;
    private String nationality;
    private String bio;
    private String avatarUrl;
    private Boolean isDeleted;
}
