package com.example.movieproject.chillmovie.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {

    private Integer episodeNumber;
    private String title;
    private String videoUrl;
    private Integer duration;
}
