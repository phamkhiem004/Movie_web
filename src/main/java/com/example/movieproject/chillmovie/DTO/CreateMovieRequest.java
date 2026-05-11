package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.Episode;
import com.example.movieproject.chillmovie.entity.MovieStatus;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.util.EnumPattern;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMovieRequest {

    @NotBlank(message = "Title can't be null")
    private String title;

    public String description;
    @Min(value = 1)
    private Integer duration;
    private String country;
    private String language;

    @Min(value = 0)
    private Integer ageLimit;
    private String trailerUrl;
    private String posterUrl;

    @EnumPattern(name = "type", regexp = "COMING_SOON|NOW_SHOWING|STOPPED")
    private MovieStatus movieStatus;

    @EnumPattern(name = "type", regexp = "SINGLE|SERIES")
    private MovieType type;


    @Valid
    private List<@NotNull Integer> genreIds;

    @Valid
    private List<ActorRequest> actors;

    @Valid
    private List<EpisodeRequest> episodes;

    public static class ActorRequest {
        public Long actorId;
        public String roleName;
    }

}
