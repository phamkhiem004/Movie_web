package com.example.movieproject.chillmovie.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class MovieSearchItemDto {
    private Long movieId;
    private String title;
    private Integer duration;
    private String description;
    private String trailerUrl;
    private String posterUrl;
    private String country;
    private String language;
    private List<String> actors;
    private List<String> genres;

    public MovieSearchItemDto() {
    }

    public MovieSearchItemDto(List<String> actors, String country, String description, Integer duration, List<String> genres, String language, Long movieId, String posterUrl, String title, String trailerUrl) {
        this.actors = actors;
        this.country = country;
        this.description = description;
        this.duration = duration;
        this.genres = genres;
        this.language = language;
        this.movieId = movieId;
        this.posterUrl = posterUrl;
        this.title = title;
        this.trailerUrl = trailerUrl;
    }

    @JsonProperty("movie_id")
    public Long getMovieIdSnakeCase() {
        return movieId;
    }

    @JsonProperty("poster_url")
    public String getPosterUrlSnakeCase() {
        return posterUrl;
    }

    @JsonProperty("actor_name")
    public String getActorName() {
        if (actors == null || actors.isEmpty()) {
            return null;
        }
        return actors.stream()
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.joining(", "));
    }
}
