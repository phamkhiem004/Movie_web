package com.example.movieproject.chillmovie.DTO;

import java.util.List;

public class SearchSuggestionsDto {

    private List<String> movies;
    private List<String> actors;
    private List<String> genres;

    public SearchSuggestionsDto(List<String> movies, List<String> actors) {
        this.movies = movies;
        this.actors = actors;
    }

    public SearchSuggestionsDto(List<String> actors, List<String> genres, List<String> movies) {
        this.actors = actors;
        this.genres = genres;
        this.movies = movies;
    }

    public List<String> getMovies() { return movies; }
    public List<String> getActors() { return actors; }
    public List<String> getGenres() { return genres; }

    public void setTracks(List<String> movies) { this.movies = movies; }
    public void setArtists(List<String> actors) { this.actors = actors; }
    public void setGenres(List<String> genres) { this.genres = genres; }
}
