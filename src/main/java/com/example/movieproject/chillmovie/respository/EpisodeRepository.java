package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode,Integer> {

    // Lấy danh sách tập theo movie
    List<Episode> findByMovieIdOrderByEpisodeNumberAsc(Long movieId);

    void deleteByMovieId(Long movieId);
}
