package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.MovieStatus;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.util.EnumPattern;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UpdateMovieRequest {

    @NotBlank(message = "Không để trống giá trị")
    private String title;

    @NotBlank(message = "Không để trống giá trị")
    private String description;



    @Min(value = 1)
    private Integer duration;

    private String country;

    private String language;

    @Min(value = 0)
    private Integer ageLimit;

    private String trailerUrl;

    private String posterUrl;

    private Boolean isDeleted;

    @EnumPattern(name = "type", regexp = "SINGLE|SERIES")
    private MovieType type;

    @EnumPattern(name = "type", regexp = "COMING_SOON|NOW_SHOWING|STOPPED")
    private MovieStatus movieStatus;


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
