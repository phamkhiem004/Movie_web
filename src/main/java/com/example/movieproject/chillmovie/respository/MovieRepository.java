package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.projection.MovieProjection;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieproject.chillmovie.entity.Movie;


import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {



    @Query("""
                SELECT DISTINCT m FROM Movie m
                LEFT JOIN FETCH m.movieActors ma
                LEFT JOIN FETCH ma.actor
                LEFT JOIN FETCH m.movieGenres mg
                LEFT JOIN FETCH mg.genre
                WHERE m.id = :movieId
            """)
    Optional<Movie> findMovieDetail(Long movieId);



    //Tìm kiếm phim theo id Actor
    @Query("SELECT m FROM Movie m JOIN MovieActor ma ON m.id=ma.movie.id WHERE ma.id.actorId = :actorId")
    List<Movie> findMoviesByActorId(@Param("actorId") Long actorId);

    //Tìm kiếm phim theo thể loại
    @Query("SELECT m FROM Movie m JOIN MovieGenre mg ON m.id = mg.movie.id WHERE mg.id.genreId = :genreId")
    List<Movie> findMoviesByGenreId(@Param("genreId") Long genreId);

    @Query("SELECT wh.movie FROM WatchHistory wh " +
            "WHERE wh.user.id = :userId " +
            "ORDER BY wh.lastWatchedAt DESC")
    List<Movie> findRecentMovies(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT " +
            "m.id as movieId, " +
            "m.title as title, " +
            "m.posterUrl as posterUrl, " +
            "e.id as episodeId, " +
            "e.episodeNumber as episodeNumber, " +
            "wh.watchedSeconds as watchedSeconds, " +
            "wh.completed as completed, " +
            "wh.lastWatchedAt as lastWatchedAt " +
            "FROM WatchHistory wh " +
            "JOIN wh.movie m " +
            "LEFT JOIN Episode e ON wh.episode.id = e.id " +
            "WHERE wh.user.id = :userId " +
            "ORDER BY wh.lastWatchedAt DESC")
    List<WatchHistoryProjection> findHistory(@Param("userId") Long userId, Pageable pageable);


    List<Movie> findByType(MovieType type);
}
