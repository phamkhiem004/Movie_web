package com.example.movieproject.chillmovie.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
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
