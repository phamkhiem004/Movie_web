package com.example.movieproject.chillmovie.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WatchHistoryDTO {
    private Long episodeId;
    private Integer watchedSeconds;
    private Boolean completed;
    private Instant lastWatchedAt;
}
