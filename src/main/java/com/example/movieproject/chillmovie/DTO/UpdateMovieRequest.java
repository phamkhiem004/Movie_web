package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.util.EnumPattern;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public class UpdateMovieRequest {

    @NotBlank(message = "Không để trống giá trị")
    public String title;

    @NotBlank(message = "Không để trống giá trị")
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
