package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.entity.FavoriteMovie;
import com.example.movieproject.chillmovie.entity.FavoriteMovieId;
import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.respository.FavoriteMovieRepository;
import com.example.movieproject.chillmovie.respository.MovieRepository;
import com.example.movieproject.chillmovie.respository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class FavoriteMovieService {

    private final FavoriteMovieRepository favoriteMovieRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    public FavoriteMovieService(FavoriteMovieRepository favoriteMovieRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.favoriteMovieRepository = favoriteMovieRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public List<FavoriteMovie> findAllByUserId(Long userId) {
        return favoriteMovieRepository.findAllByUserId(userId);
    }

    //Thêm phim yêu thích
    @Transactional
    public void likeMovie(Long userId, Long movieId) {

        if (favoriteMovieRepository.findByUserAndMovieId(userId, movieId).isPresent()) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        FavoriteMovieId favoriteMovieId = new FavoriteMovieId(userId, movieId);

        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.setId(favoriteMovieId);
        favoriteMovie.setUser(user);
        favoriteMovie.setMovie(movie);
        favoriteMovie.setCreatedAt(Instant.now());

        favoriteMovieRepository.save(favoriteMovie);
    }

    //Xóa phim yêu thích
    @Transactional
    public void unlikeMovie(Long userId, Long movieId) {
        favoriteMovieRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    public boolean checkIfUserLikedMovie(Long userId, Long movieId) {
        return favoriteMovieRepository.checkIfUserLikedMovie(userId, movieId);
    }


}
