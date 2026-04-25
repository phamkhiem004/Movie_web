package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.MovieDTO;
import com.example.movieproject.chillmovie.entity.FavoriteMovie;
import com.example.movieproject.chillmovie.entity.RestResponse;
import com.example.movieproject.chillmovie.service.FavoriteMovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FavoriteMovieController {
    private final FavoriteMovieService favoriteMovieService;


    public FavoriteMovieController(FavoriteMovieService favoriteMovieService) {
        this.favoriteMovieService = favoriteMovieService;
    }

    // List phim yêu thích
    @GetMapping("/favorite/user/{id}")
    public ResponseEntity<List<MovieDTO>> getFavoriteMoviesByUser(@PathVariable Long id) {
        List<MovieDTO> movies = favoriteMovieService.getAllFavouriteMovies(id);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    // API Like phim
    @PostMapping("/like/{userId}/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> likeMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        Boolean liked = favoriteMovieService.likeMovie(userId, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage(liked ? "Like thành công" : "Đã like trước đó");
        res.setData(liked); // Tùy chọn

        return ResponseEntity.ok(res);
    }

    // API Unlike phim
    @DeleteMapping("/unlike/{userId}/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> unlikeMovie(@PathVariable Long userId, @PathVariable Long movieId) {

        Boolean unliked = favoriteMovieService.unlikeMovie(userId, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage(unliked ? " Hủy Like thành công" : "Chưa like trước đó");
        res.setData(unliked); // Tùy chọn

        return ResponseEntity.ok(res);

    }

    // API Check trạng thái Like
    @GetMapping("/status/{userId}/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> checkLikeStatus(@PathVariable Long userId, @PathVariable Long movieId) {
        Boolean check = favoriteMovieService.checkIfUserLikedMovie(userId, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage("Thành công");
        res.setData(check);

        return ResponseEntity.ok(res);
    }
}
