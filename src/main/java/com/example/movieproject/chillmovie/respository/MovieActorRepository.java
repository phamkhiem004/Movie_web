package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.MovieActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieActorRepository extends JpaRepository<MovieActor,Long> {

    @Modifying
    @Query("DELETE FROM MovieActor ma WHERE ma.movie.id = :movieId")
    void deleteByMovieId(@Param("movieId") Long movieId);
}
