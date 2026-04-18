package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre,Long> {

    @Modifying
    @Query("DELETE FROM MovieGenre mg WHERE mg.movie.id = :movieId")
    void deleteByMovieId(@Param("movieId") Long movieId);
}
