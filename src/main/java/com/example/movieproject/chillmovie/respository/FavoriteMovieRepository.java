package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.FavoriteMovie;
import com.example.movieproject.chillmovie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie,Long> {

    //Tìm kiếm phim yêu thích bởi user
    @Query("SELECT fm.movie FROM FavoriteMovie fm WHERE fm.user.id = :userId")
    List<Movie> findFavoriteByUserId(@Param("userId") Long userId);

    @Query("SELECT fm.movie FROM FavoriteMovie fm WHERE fm.user.id = :userId AND fm.movie.id = :movieId")
    Optional<FavoriteMovie> findByUserAndMovieId(@Param("userId")Long userId, @Param("movieId") Long movieId);

    @Modifying
    @Query("delete from FavoriteMovie fm WHERE fm.user.id = :userId AND fm.movie.id = :movieId")
    void deleteByUserIdAndMovieId(@Param("userId")Long userId, @Param("movieId") Long movieId);

    @Query("SELECT COUNT(fm) > 0 FROM FavoriteMovie fm WHERE fm.user.id = :userId AND fm.movie.id = :movieId")
    boolean checkIfUserLikedMovie(@Param("userId") Long userId, @Param("movieId") Long movieId);
}
