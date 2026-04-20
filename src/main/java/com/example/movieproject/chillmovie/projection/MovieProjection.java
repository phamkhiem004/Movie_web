package com.example.movieproject.chillmovie.projection;

public interface MovieProjection {
    Long getMovieId();
    String getTitle();
    String getPosterUrl();

    Integer getWatchedSeconds();
    Boolean getCompleted();
}
