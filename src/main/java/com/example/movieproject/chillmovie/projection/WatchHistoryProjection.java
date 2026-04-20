package com.example.movieproject.chillmovie.projection;

import java.time.Instant;

public interface WatchHistoryProjection {
    Long getMovieId();
    String getTitle();
    String getPosterUrl();
    Long getEpisodeId();
    Integer getEpisodeNumber();
    Integer getWatchedSeconds();
    Boolean getCompleted();
    Instant getLastWatchedAt();
}
