package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.Episode;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.util.EnumPattern;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class CreateMovieRequest {

    @NotBlank(message = "Title can't be null")
    public String title;

    public String description;
    @Min(value = 1)
    public Integer duration;
    public String country;
    public String language;

    @Min(value = 0)
    public Integer ageLimit;
    public String trailerUrl;
    public String posterUrl;

    @EnumPattern(name = "type", regexp = "SINGLE|SERIES")
    public MovieType type;


    @Valid
    public List<@NotNull Integer> genreIds;

    @Valid
    public List<ActorRequest> actors;

    @Valid
    public List<EpisodeRequest> episodes;

    public static class ActorRequest {
        public Long actorId;
        public String roleName;
    }

}
