package com.example.movieproject.chillmovie.controller;

import java.util.List;

import com.example.movieproject.chillmovie.DTO.CreateMovieRequest;
import com.example.movieproject.chillmovie.DTO.MovieDTO;
import com.example.movieproject.chillmovie.DTO.UpdateMovieRequest;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.projection.MovieProjection;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.service.MovieService;
import com.example.movieproject.chillmovie.util.error.IdInvalidException;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    //Hiển thị khi chưa login
    @GetMapping("/movies")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    //Hiển thị khi đã login
    @GetMapping("/movies/user/{id}")
    public ResponseEntity<List<MovieProjection>> getMovie(@PathVariable @Min(value = 1,message = "User Id must be greater than 0") Long id) throws IdInvalidException {
        List<MovieProjection> movies = movieService.getALlMovieWithHistory(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    // Thông tin phim khi chưa login
    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDTO> getMovieByID(@PathVariable @Min(value = 1,message = "Movie Id must be greater than 0") Long id) {
        MovieDTO movie = movieService.getMovieByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    //Thông tin phim khi đã login
    @GetMapping("/movie/{id}/user/{userid}")
    public ResponseEntity<MovieDTO> getMovieByUserID(@PathVariable @Min(value = 1,message = "Movie Id must be greater than 1") Long id, @PathVariable @Min(value = 1,message = "User Id must be greater than 1") Long userid) {
        MovieDTO movie = movieService.getMovieDetail(id, userid);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    //Tạo mới phim
    @PostMapping("/movie/create")
    public ResponseEntity<MovieDTO> createMovie(
            @Valid @RequestBody CreateMovieRequest request) {

        MovieDTO createdMovie = this.movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);

    }


    //Xóa phim => sau update là Unactive
    @DeleteMapping("/movie/{id}/delete")
    public ResponseEntity<Object> deleteMovie(@PathVariable @Min(value = 1,message = "Movie Id must be greater than 0") Long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("ID must be less than 1500");
        }

        movieService.deleteMovie(id);
        return ResponseEntity.ok().body(java.util.Map.of("message", "Movie deleted successfully"));

    }


    //Update phim
    @PutMapping("/movie/{id}/update")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable @Min(value = 1,message = "Movie Id must be greater than 0") Long id, @Valid @RequestBody UpdateMovieRequest movie) {
        MovieDTO updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMovie);
    }


    //Lấy list phim theo actor
    @GetMapping("/movies/actor/{id}")
    public ResponseEntity<List<MovieDTO>> getMovieByActorID(@PathVariable @Min(value = 1,message = "Actor Id must be greater than 0") Long id) {
        List<MovieDTO> movies = movieService.findMovieByActorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    //Lấy list phim theo thể loại
    @GetMapping("/movies/genre/{id}")
    public ResponseEntity<List<MovieDTO>> getMovieByGenreID(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.findMovieByGenreId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    // List lịch sử xem phim của user
    @GetMapping("/recent/user/{id}")
    public ResponseEntity<List<WatchHistoryProjection>> getMovieHistoryByUser(@PathVariable Long id) {
        List<WatchHistoryProjection> history = movieService.getAllHistoryMovies(id);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    // List phim theo phim lẻ/bộ
    @GetMapping("/movies/type")
    public ResponseEntity<List<MovieDTO>> getAllMoviesByType(@RequestParam MovieType type) {
        List<MovieDTO> movies = movieService.findMovieByType(type);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


}
