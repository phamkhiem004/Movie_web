package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.WatchHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    public String title;
    public String description;
    public Integer duration;
    public String country;
    public String language;
    public Integer ageLimit;
    public String trailerUrl;
    public String posterUrl;
    private List<String> actors;
    private List<String> genres;
    private WatchHistoryDTO continueWatching;

}
