package com.example.movieproject.chillmovie.DTO;

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
    public Long id;
    public String title;
    public String description;
    public Integer duration;
    public String country;
    public String language;
    public Integer ageLimit;
    public String trailerUrl;
    public String posterUrl;
    public MovieType type;
    public LocalDate releaseDate;
    public List<String> actors;
    public List<String> genres;
    public  List<EpisodeDTO> episodes;
    public WatchHistoryDTO continueWatching;

}
