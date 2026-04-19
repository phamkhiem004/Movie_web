package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.DTO.MovieHistoryDto;
import com.example.movieproject.chillmovie.entity.FavoriteMovie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieproject.chillmovie.entity.Movie;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    //Tìm kiếm phim yêu thích bởi user
    @Query("SELECT fm FROM FavoriteMovie fm WHERE fm.user.id = :userId")
    List<FavoriteMovie> findByUserId(@Param("userId") Long userId);


    //Tìm kiếm phim theo id Actor
    @Query("SELECT m FROM Movie m JOIN MovieActor ma ON m.id=ma.movie.id WHERE ma.id.actorId = :actorId")
    List<Movie> findMoviesByActorId(@Param("actorId") Long actorId);

    //Tìm kiếm phim theo thể loại
    @Query("SELECT m FROM Movie m JOIN MovieGenre mg ON m.id = mg.movie.id WHERE mg.id.genreId = :genreId")
    List<Movie> findMoviesByGenreId(@Param("genreId") Long genreId);

    //Tìm kiếm lịch sử xem phim theo user
    @Query("SELECT new com.example.movieproject.chillmovie.DTO.MovieHistoryDto(" +
            "m.id, m.title, m.posterUrl, wh.lastWatchedAt) " +
            "FROM WatchHistory wh " +
            "JOIN wh.movie m " +
            "WHERE wh.user.id = :userId " +
            "ORDER BY wh.lastWatchedAt DESC")
    List<MovieHistoryDto> findRecentMovies(@Param("userId") Long userId, Pageable pageable);



}
