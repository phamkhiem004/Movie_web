package com.example.movieproject.chillmovie.DTO;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class CreateActorRequest {

    @NotBlank(message = "Name can't be null'")
    public String name;
    public LocalDate birthDate;
    public String nationality;
    public String bio;
    public String avatarUrl;

}
