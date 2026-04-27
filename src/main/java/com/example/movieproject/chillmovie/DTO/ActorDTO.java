package com.example.movieproject.chillmovie.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActorDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String nationality;
    private String bio;
    private String avatarUrl;

}
