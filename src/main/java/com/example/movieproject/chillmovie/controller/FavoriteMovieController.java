package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.entity.FavoriteMovie;
import com.example.movieproject.chillmovie.entity.RestResponse;
import com.example.movieproject.chillmovie.service.FavoriteMovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FavoriteMovieController {
    private final FavoriteMovieService favoriteMovieService;


    public FavoriteMovieController(FavoriteMovieService favoriteMovieService) {
        this.favoriteMovieService = favoriteMovieService;
    }

    @GetMapping("/favorite/{id}")
    public ResponseEntity<List<FavoriteMovie>> getFavoriteMovies(@PathVariable Long id) {
        List<FavoriteMovie> favoriteMovies = favoriteMovieService.findAllByUserId(id);
        return ResponseEntity.ok().body(favoriteMovies);
    }

    // API Like phim
    @PostMapping("/like/{userId}/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> likeMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        favoriteMovieService.likeMovie(userId, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage("Thành công");
        res.setData(true); // Tùy chọn

        return ResponseEntity.ok(res);
    }

    // API Unlike phim
    @DeleteMapping("/unlike/{userId}/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> unlikeMovie(@PathVariable Long userId, @PathVariable Long movieId) {

        favoriteMovieService.unlikeMovie(userId, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage("Thành công");
        res.setData(true); // Tùy chọn

        return ResponseEntity.ok(res);

    }

    // API Check trạng thái Like
    @GetMapping("/status/{userId}/{movieId}")
    public ResponseEntity<Boolean> checkLikeStatus(@PathVariable Long userId, @PathVariable Long movieId) {
        boolean isLiked = favoriteMovieService.checkIfUserLikedMovie(userId, movieId);
        return ResponseEntity.ok(isLiked);
    }
}
