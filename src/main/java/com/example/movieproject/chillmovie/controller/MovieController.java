package com.example.movieproject.chillmovie.controller;

import java.util.List;

import com.example.movieproject.chillmovie.DTO.CreateMovieRequest;
import com.example.movieproject.chillmovie.DTO.MovieDTO;
import com.example.movieproject.chillmovie.DTO.UpdateMovieRequest;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import com.example.movieproject.chillmovie.DTO.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.movieproject.chillmovie.service.MovieService;
import com.example.movieproject.chillmovie.util.error.IdInvalidException;

@RequestMapping("/movie")
@Tag(name = "Movie Controller")
@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }


    @Operation(summary = "Get all movies for user", description = "Api get all movies")
    @GetMapping("/")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    @Operation(summary = "Get all movies for admin", description = "Api get all movies for admin dashboard")
    @GetMapping("/admin")
    public ResponseEntity<List<MovieDTO>> getAdminMovies() {
        List<MovieDTO> movies = movieService.getAdminMovies();
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    @Operation(summary = "Get movie detail", description = "API get movie by Id")
    @GetMapping("/details/{id}")
    public ResponseEntity<MovieDTO> getMovieByID(@PathVariable @Min(value = 1, message = "Movie Id must be greater than 0") Long id,
                                                 @AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt) {
        CustomUserDetails user = null;
        if (jwt != null) {
            user = new CustomUserDetails();
            Long userId = jwt.getClaim("userId");
            user.setId(userId);
        }
        assert user != null;
        MovieDTO movie = movieService.getMovieDetail(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }


    @Operation(summary = "Create new movie", description = "Api create new movie")
    @PostMapping("/create")
    public ResponseEntity<MovieDTO> createMovie(
            @Valid @RequestBody CreateMovieRequest request) {

        MovieDTO createdMovie = this.movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);

    }


    @Operation(summary = "Update movie", description = "API update movie")
    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable @Min(value = 1, message = "Movie Id must be greater than 0") Long id, @Valid @RequestBody UpdateMovieRequest movie) {
        MovieDTO updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMovie);
    }


    @Operation(summary = "Get list movie by actor", description = "API get list movie by actor")
    @GetMapping("/actor/{id}")
    public ResponseEntity<List<MovieDTO>> getMovieByActorID(@PathVariable @Min(value = 1, message = "Actor Id must be greater than 0") Long id) {
        List<MovieDTO> movies = movieService.findMovieByActorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    @Operation(summary = "Get list movie by genre", description = "API get list movie by genre")
    @GetMapping("/genre/{id}")
    public ResponseEntity<List<MovieDTO>> getMovieByGenreID(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.findMovieByGenreId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    @Operation(summary = "Get list history movie", description = "API get list history movie")
    @GetMapping("/recent")
    public ResponseEntity<List<WatchHistoryProjection>> getMovieHistoryByUser(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt) {
        CustomUserDetails user = null;
        if (jwt != null) {
            user = new CustomUserDetails();
            Long userId = jwt.getClaim("userId");
            user.setId(userId);
        }
        List<WatchHistoryProjection> history = movieService.getAllHistoryMovies(user);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    @Operation(summary = "Get list movie by type", description = "API get list movie by type")
    @GetMapping("/type")
    public ResponseEntity<List<MovieDTO>> getAllMoviesByType(@RequestParam MovieType type) {
        List<MovieDTO> movies = movieService.findMovieByType(type);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


}
