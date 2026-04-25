package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.DTO.MovieDTO;
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

    //Danh sách phim yêu thích
    public List<MovieDTO> getAllFavouriteMovies(Long userId) {
        List<Movie> movies = favoriteMovieRepository.findFavoriteByUserId(userId);

        return movies.stream().map(m -> {
            MovieDTO dto = new MovieDTO();
            dto.setId(m.getId());
            dto.setTitle(m.getTitle());
            dto.setDescription(m.getDescription());
            dto.setDuration(m.getDuration());
            dto.setCountry(m.getCountry());
            dto.setLanguage(m.getLanguage());
            dto.setAgeLimit(m.getAgeLimit());
            dto.setTrailerUrl(m.getTrailerUrl());
            dto.setPosterUrl(m.getPosterUrl());
            return dto;
        }).toList();
    }

    //Thêm phim yêu thích
    @Transactional
    public boolean likeMovie(Long userId, Long movieId) {

        if (favoriteMovieRepository.findByUserAndMovieId(userId, movieId).isPresent()) {
            return false;
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

        return true;
    }

    @Transactional
    public boolean unlikeMovie(Long userId, Long movieId) {

        if (!favoriteMovieRepository.findByUserAndMovieId(userId, movieId).isPresent()) {
            return false; // chưa like mà đòi unlike
        }

        favoriteMovieRepository.deleteByUserIdAndMovieId(userId, movieId);
        return true;
    }

    public boolean checkIfUserLikedMovie(Long userId, Long movieId) {
        return favoriteMovieRepository.checkIfUserLikedMovie(userId, movieId);
    }


}
