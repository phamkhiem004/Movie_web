package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode,Integer> {

    // Lấy danh sách tập theo movie
    List<Episode> findByMovieIdOrderByEpisodeNumberAsc(Long movieId);

    void deleteByMovieId(Long movieId);
}
