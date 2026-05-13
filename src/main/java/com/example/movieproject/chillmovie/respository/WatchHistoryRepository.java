package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    @Query("""
            SELECT wh FROM WatchHistory wh
            LEFT JOIN FETCH wh.episode
            LEFT JOIN FETCH wh.movie
            LEFT JOIN FETCH wh.user
            WHERE wh.movie.id = :movieId AND wh.user.id = :userId
            """)
    List<WatchHistory> findWatchHistory(@Param("movieId") Long movieId,
                                        @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO watch_history (user_id, movie_id, episode_id, watched_seconds, completed, last_watched_at)
            VALUES (:userId, :movieId, :episodeId, :seconds, :completed, NOW())
            ON DUPLICATE KEY UPDATE
                watched_seconds  = :seconds,
                completed        = :completed,
                last_watched_at  = NOW()
            """, nativeQuery = true)
    void upsert(@Param("userId") Long userId,
                @Param("movieId") Long movieId,
                @Param("episodeId") Long episodeId,
                @Param("seconds") int seconds,
                @Param("completed") boolean completed);
}


