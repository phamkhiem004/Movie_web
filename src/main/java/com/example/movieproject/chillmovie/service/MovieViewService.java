package com.example.movieproject.chillmovie.service;


import com.example.movieproject.chillmovie.DTO.WatchHistoryDTO;
import com.example.movieproject.chillmovie.DTO.WatchHistoryRequest;
import com.example.movieproject.chillmovie.entity.Episode;
import com.example.movieproject.chillmovie.entity.MovieView;
import com.example.movieproject.chillmovie.respository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MovieViewService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MovieRepository movieRepository;
    private final EpisodeRepository episodeRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final MovieViewRepository movieViewRepository;
    private final UserRepository userRepository;

    public void recordView(Long movieId, Long userId, String ip) {
        String msg = movieId + "|" + userId + "|" + ip;
        kafkaTemplate.send("movieView", msg);
        log.info("Sent movieView event: movieId={} userId={}", movieId, userId);
    }

    public void updateWatchHistory(Long userId, WatchHistoryRequest req) {
        int duration;
        if (req.getEpisodeId() != null) {
            duration = episodeRepository.findById(req.getEpisodeId())
                    .map(e -> e.getDuration() != null ? e.getDuration() : 0)
                    .orElse(0);
        } else {
            duration = movieRepository.findById(req.getMovieId())
                    .map(m -> m.getDuration() != null ? m.getDuration() : 0)
                    .orElse(0);
        }

        String episodeId = req.getEpisodeId() != null
                ? req.getEpisodeId().toString() : "null";

        String msg = userId + "|" + req.getMovieId() + "|"
                + episodeId + "|" + req.getWatchedSeconds() + "|" + duration;

        kafkaTemplate.send("watchHistory", msg);
        log.info("Sent watchHistory event: userId={} movieId={} seconds={}",
                userId, req.getMovieId(), req.getWatchedSeconds());

    }
    @KafkaListener(topics = "watchHistory", groupId = "watch-history-group")
    public void handleWatchHistory(String message) {
        String[] arr = message.split("\\|");
        Long userId    = Long.parseLong(arr[0]);
        Long movieId   = Long.parseLong(arr[1]);
        Long episodeId = arr[2].equals("null") ? 0L : Long.parseLong(arr[2]);
        int  seconds   = Integer.parseInt(arr[3]);
        int    duration  = Integer.parseInt(arr[4]);
        int durationSeconds = duration * 60;
        boolean completed = durationSeconds > 0
                && seconds >= (int)(durationSeconds * 0.9);
        watchHistoryRepository.upsert(userId, movieId, episodeId, seconds, completed);
        log.info("UPSERT OK — userId={} completed={}", userId, completed);
    }


    @KafkaListener(topics = "movieView", groupId = "movie-view-group")
    public void handleMovieView(String message) {
        String[] arr = message.split("\\|");
        Long movieId = Long.parseLong(arr[0]);
        Long userId  = arr[1].equals("null") ? null : Long.parseLong(arr[1]);

        MovieView view = new MovieView();
        view.setMovie(movieRepository.getReferenceById(movieId));
        view.setUser(userId != null ? userRepository.getReferenceById(userId) : null);
        view.setIpAddress(arr[2]);
        movieViewRepository.save(view);
        log.info("Ghi view phim {} OK", movieId);
    }


}
