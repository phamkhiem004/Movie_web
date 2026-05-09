package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.DTO.CustomUserDetails;
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
    public List<MovieDTO> getAllFavouriteMovies(CustomUserDetails user) {
        List<Movie> movies = favoriteMovieRepository.findFavoriteByUserId(user.getId());

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
    public boolean likeMovie(CustomUserDetails user, Long movieId) {

        if (favoriteMovieRepository.findByUserAndMovieId(user.getId(), movieId).isPresent()) {
            return false;
        }

        User u = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        FavoriteMovieId favoriteMovieId = new FavoriteMovieId(user.getId(), movieId);

        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.setId(favoriteMovieId);
        favoriteMovie.setUser(u);
        favoriteMovie.setMovie(movie);
        favoriteMovie.setCreatedAt(Instant.now());

        favoriteMovieRepository.save(favoriteMovie);

        return true;
    }

    @Transactional
    public boolean unlikeMovie(CustomUserDetails user, Long movieId) {

        if (!favoriteMovieRepository.findByUserAndMovieId(user.getId(), movieId).isPresent()) {
            return false; // chưa like mà đòi unlike
        }

        favoriteMovieRepository.deleteByUserIdAndMovieId(user.getId(), movieId);
        return true;
    }

    public boolean checkIfUserLikedMovie(CustomUserDetails user, Long movieId) {
        return favoriteMovieRepository.checkIfUserLikedMovie(user.getId(), movieId);
    }


}
