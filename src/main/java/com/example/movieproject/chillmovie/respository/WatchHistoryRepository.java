package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
