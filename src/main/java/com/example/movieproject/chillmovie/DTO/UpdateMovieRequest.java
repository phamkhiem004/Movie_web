package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.MovieType;

import java.util.List;

public class UpdateMovieRequest {
    public String title;
    public String description;
    public Integer duration;
    public String country;
    public String language;
    public Integer ageLimit;
    public String trailerUrl;
    public String posterUrl;
    public MovieType type;

    public List<Integer> genreIds;
    public List<ActorRequest> actors;
    public List<EpisodeRequest> episodes;

    public static class ActorRequest {
        public Long actorId;
        public String roleName;
    }

}
