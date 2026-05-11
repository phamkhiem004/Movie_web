package com.example.movieproject.chillmovie.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EpisodeRequest {
    private Integer episodeNumber;
    private String title;
    private String videoUrl;
    private Integer duration;
}
