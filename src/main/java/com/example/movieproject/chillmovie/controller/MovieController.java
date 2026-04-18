package com.example.movieproject.chillmovie.controller;

import java.util.List;

import com.example.movieproject.chillmovie.DTO.CreateMovieRequest;
import com.example.movieproject.chillmovie.DTO.UpdateMovieRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.service.MovieService;
import com.example.movieproject.chillmovie.service.error.IdInvalidException;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
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

}
