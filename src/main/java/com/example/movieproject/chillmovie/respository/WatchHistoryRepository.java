package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory,Integer> {
    @Query("""
    SELECT wh FROM WatchHistory wh
    LEFT JOIN FETCH wh.episode
    WHERE wh.movie.id = :movieId AND wh.user.id = :userId
""")
    List<WatchHistory> findWatchHistory(Long movieId, Long userId);
}
