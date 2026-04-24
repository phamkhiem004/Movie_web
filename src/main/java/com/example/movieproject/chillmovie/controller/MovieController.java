package com.example.movieproject.chillmovie.controller;

import java.util.List;

import com.example.movieproject.chillmovie.DTO.CreateMovieRequest;
import com.example.movieproject.chillmovie.DTO.MovieDTO;
import com.example.movieproject.chillmovie.DTO.UpdateMovieRequest;
import com.example.movieproject.chillmovie.projection.MovieProjection;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.service.MovieService;
import com.example.movieproject.chillmovie.service.error.IdInvalidException;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    //Hiển thị khi chưa login
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    //Hiển thị khi đã login
    @GetMapping("/movies/user/{id}")
    public ResponseEntity<List<MovieProjection>> getMovie(@PathVariable Long id) throws IdInvalidException {
        List<MovieProjection> movies = movieService.getALlMovieWithHistory(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @PostMapping("/movie/create")
    public ResponseEntity<Movie> createMovie(
            @RequestBody CreateMovieRequest request) {

        Movie createdMovie = this.movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);

    }

    @DeleteMapping("/movies/{id}/delete")
    public ResponseEntity<Object> deleteMovie(@PathVariable Long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("ID must be less than 1500");
        }

        movieService.deleteMovie(id);
        return ResponseEntity.ok().body(java.util.Map.of("message", "Movie deleted successfully"));

    }

    @GetMapping("movies/{id}")
    public ResponseEntity<Movie> getMovieByID(@PathVariable Long id) {
        Movie movie = movieService.getMovieByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PutMapping("/movies/{id}/update")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody UpdateMovieRequest movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMovie);
    }

    @GetMapping("/movies/actor/{id}")
    public ResponseEntity<List<MovieDTO>> getMovieByActorID(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.findMovieByActorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/movies/genre/{id}")
    public ResponseEntity<List<MovieDTO>> getMovieByGenreID(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.findMovieByGenreId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/movie/{id}/user/{userid}")
    public ResponseEntity<MovieDTO> getMovieByUserID(@PathVariable Long id, @PathVariable Long userid) {
        MovieDTO movie = movieService.getMovieDetail(id, userid);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping("/recent/user/{id}")
    public ResponseEntity<List<WatchHistoryProjection>> getMovieHistoryByUser(@PathVariable Long id) {
        List<WatchHistoryProjection> history = movieService.getAllHistoryMovies(id);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

}
