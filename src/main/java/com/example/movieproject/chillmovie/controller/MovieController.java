package com.example.movieproject.chillmovie.controller;

import java.util.List;

import com.example.movieproject.chillmovie.DTO.CreateMovieRequest;
import com.example.movieproject.chillmovie.DTO.MovieDTO;
import com.example.movieproject.chillmovie.DTO.UpdateMovieRequest;
import com.example.movieproject.chillmovie.entity.MovieType;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.projection.MovieProjection;
import com.example.movieproject.chillmovie.projection.WatchHistoryProjection;
import com.example.movieproject.chillmovie.util.CustomUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.service.MovieService;
import com.example.movieproject.chillmovie.util.error.IdInvalidException;

@RequestMapping("/movie")
@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    //Hiển thị khi chưa login
    @GetMapping("/")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }





    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieByID(@PathVariable @Min(value = 1,message = "Movie Id must be greater than 0") Long id,
                                                 @AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt) {
        CustomUserDetails user = null;
        if (jwt != null) {
            user = new CustomUserDetails();
            // Lấy "userId" từ claim mà bạn đã đặt trong SecurityUtil
            Long userId = jwt.getClaim("userId");
            user.setId(userId);
        }
        MovieDTO movie = movieService.getMovieDetail(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }



    //Tạo mới phim
    @PostMapping("/create")
    public ResponseEntity<MovieDTO> createMovie(
            @Valid @RequestBody CreateMovieRequest request) {

        MovieDTO createdMovie = this.movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);

    }


    //Xóa phim => sau update là Unactive
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteMovie(@PathVariable @Min(value = 1,message = "Movie Id must be greater than 0") Long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("ID must be less than 1500");
        }

        movieService.deleteMovie(id);
        return ResponseEntity.ok().body(java.util.Map.of("message", "Movie deleted successfully"));

    }


    //Update phim
    @PutMapping("/{id}/update")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable @Min(value = 1,message = "Movie Id must be greater than 0") Long id, @Valid @RequestBody UpdateMovieRequest movie) {
        MovieDTO updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMovie);
    }


    //Lấy list phim theo actor
    @GetMapping("/actor/{id}")
    public ResponseEntity<List<MovieDTO>> getMovieByActorID(@PathVariable @Min(value = 1,message = "Actor Id must be greater than 0") Long id) {
        List<MovieDTO> movies = movieService.findMovieByActorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


    //Lấy list phim theo thể loại
    @GetMapping("/genre/{id}")
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
    @GetMapping("/type")
    public ResponseEntity<List<MovieDTO>> getAllMoviesByType(@RequestParam MovieType type) {
        List<MovieDTO> movies = movieService.findMovieByType(type);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }


}
