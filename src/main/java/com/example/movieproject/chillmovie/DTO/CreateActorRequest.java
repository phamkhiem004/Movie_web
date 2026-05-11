package com.example.movieproject.chillmovie.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CreateActorRequest {

    @NotBlank(message = "Name can't be null'")
    private String name;
    private LocalDate birthDate;
    private String nationality;
    private String bio;
    private String avatarUrl;

}
