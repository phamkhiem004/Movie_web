package com.example.movieproject.chillmovie.service;

import java.util.List;
import java.util.Optional;

import com.example.movieproject.chillmovie.entity.*;
import com.example.movieproject.chillmovie.respository.MovieActorRepository;
import com.example.movieproject.chillmovie.respository.MovieGenreRepository;
import org.springframework.stereotype.Service;

import com.example.movieproject.chillmovie.respository.MovieRepository;
import com.example.movieproject.chillmovie.DTO.CreateMovieRequest;
import com.example.movieproject.chillmovie.DTO.UpdateMovieRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieActorRepository movieActorRepository;

    public MovieService(MovieRepository movieRepository, MovieGenreRepository movieGenreRepository, MovieActorRepository movieActorRepository) {
        this.movieRepository = movieRepository;
        this.movieGenreRepository = movieGenreRepository;
        this.movieActorRepository = movieActorRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieByID(Long id) {
        Optional<Movie> movieOptional = movieRepository.findById(id);// do findById trả về Optional để tránh lỗi
                                                                     // NullPointerException khi không tìm thấy movie
                                                                     // với id đó
        // nếu movie tồn tại, trả về movie đó
        return movieOptional.orElse(null);
    }

    public Movie createMovie(CreateMovieRequest request) {

        // 1. Lưu movie trước
        Movie movie = new Movie();
        movie.setTitle(request.title);
        movie.setDuration(request.duration);
        movie.setLanguage(request.language);
        movie.setCountry(request.country);
        movie.setAgeLimit(request.ageLimit);
        movie.setDescription(request.description);
        movie.setTrailerUrl(request.trailerUrl);
        movie.setPosterUrl(request.posterUrl);

        movie = movieRepository.save(movie);

        // 2. Lưu genres (movie_genres)
        for (Integer genreId : request.genreIds) {
            MovieGenre mg = new MovieGenre();

            MovieGenreId id = new MovieGenreId();
            id.setMovieId(movie.getId());
            id.setGenreId(genreId);

            mg.setId(id);
            mg.setMovie(movie);

            movieGenreRepository.save(mg);
        }

        // 3. Lưu actors (movie_actors)
        for (CreateMovieRequest.ActorRequest a : request.actors) {
            MovieActor ma = new MovieActor();

            MovieActorId id = new MovieActorId();
            id.setMovieId(movie.getId());
            id.setActorId(a.actorId);

            ma.setId(id);
            ma.setMovie(movie);
            ma.setRoleName(a.roleName);

            movieActorRepository.save(ma);
        }

        return movie;
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Transactional
    public Movie updateMovie(Long movieId, UpdateMovieRequest request) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));


        movie.setTitle(request.title);
        movie.setDescription(request.description);
        movie.setTrailerUrl(request.trailerUrl);
        movie.setPosterUrl(request.posterUrl);
        movie.setDuration(request.duration);
        movie.setLanguage(request.language);
        movie.setCountry(request.country);
        movie.setAgeLimit(request.ageLimit);

        movieRepository.save(movie);


        movieGenreRepository.deleteByMovieId(movieId);

        for (Integer genreId : request.genreIds) {
            MovieGenre mg = new MovieGenre();

            MovieGenreId id = new MovieGenreId();
            id.setMovieId(movieId);
            id.setGenreId(genreId);

            mg.setId(id);
            mg.setMovie(movie);

            movieGenreRepository.save(mg);
        }

        movieActorRepository.deleteByMovieId(movieId);

        for (UpdateMovieRequest.ActorRequest a : request.actors) {
            MovieActor ma = new MovieActor();

            MovieActorId id = new MovieActorId();
            id.setMovieId(movieId);
            id.setActorId(a.actorId);

            ma.setId(id);
            ma.setMovie(movie);
            ma.setRoleName(a.roleName);

            movieActorRepository.save(ma);
        }

        return movie;
    }

}
