package com.example.movieproject.chillmovie.DTO;

import java.time.Instant;

public class MovieHistoryDto {
    private Long movieId;
    private String title;
    private String posterUrl;
    private Instant lastWatchedAt;

    public MovieHistoryDto(Long movieId, String title, String posterUrl, Instant lastWatchedAt) {
        this.movieId = movieId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.lastWatchedAt = lastWatchedAt;
    }
}
