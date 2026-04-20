package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.FavoriteMovie;
import com.example.movieproject.chillmovie.projection.MovieProjection;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieproject.chillmovie.entity.Movie;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    //Tìm kiếm phim yêu thích bởi user
    @Query("SELECT fm.movie FROM FavoriteMovie fm WHERE fm.user.id = :userId")
    List<Movie> findFavoriteByUserId(@Param("userId") Long userId);

    //Tìm tất cả các phim với lịch sử xem
    @Query("SELECT " +
            "m.id AS movieId, " +
            "m.title AS title, " +
            "m.posterUrl AS posterUrl, " +
            "wh.watchedSeconds AS watchedSeconds, " +
            "wh.completed AS completed " +
            "FROM Movie m " +
            "LEFT JOIN WatchHistory wh ON wh.movie = m AND wh.user.id = :userId")
    List<MovieProjection> findAllMoviesWithHistory(@Param("userId") Long userId);


    //Tìm kiếm phim theo id Actor
    @Query("SELECT m FROM Movie m JOIN MovieActor ma ON m.id=ma.movie.id WHERE ma.id.actorId = :actorId")
    List<Movie> findMoviesByActorId(@Param("actorId") Long actorId);

    //Tìm kiếm phim theo thể loại
    @Query("SELECT m FROM Movie m JOIN MovieGenre mg ON m.id = mg.movie.id WHERE mg.id.genreId = :genreId")
    List<Movie> findMoviesByGenreId(@Param("genreId") Long genreId);

    @Query("SELECT wh.movie FROM WatchHistory wh " +
            "WHERE wh.user.id = :userId " +
            "ORDER BY wh.lastWatchedAt DESC")
    List<Movie> findRecentMovies(@Param("userId") Long userId);

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
    List<WatchHistoryProjection> findHistory(@Param("userId")Long userId, Pageable pageable);





}
