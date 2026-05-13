package com.example.movieproject.chillmovie.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@RequiredArgsConstructor
public class WatchHistoryRequest {
    private Long episodeId;
    private Long movieId;
    private Integer watchedSeconds;
    private Integer duration;
}
