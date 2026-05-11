package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.MovieStatus;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.entity.WatchHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private String country;
    private String language;
    private Integer ageLimit;
    private String trailerUrl;
    private String posterUrl;
    private MovieStatus status;
    private Boolean isDeleted;
    private MovieType type;
    private LocalDate releaseDate;
    private List<String> actors;
    private List<String> genres;
    private  List<EpisodeDTO> episodes;
    private WatchHistoryDTO continueWatching;

}
