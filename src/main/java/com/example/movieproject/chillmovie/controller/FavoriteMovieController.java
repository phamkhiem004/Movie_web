package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.CustomUserDetails;
import com.example.movieproject.chillmovie.DTO.MovieDTO;
import com.example.movieproject.chillmovie.entity.FavoriteMovie;
import com.example.movieproject.chillmovie.entity.RestResponse;
import com.example.movieproject.chillmovie.service.FavoriteMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/favorite")
@Tag(name = "Favorite Movie Controller")
@RestController
public class FavoriteMovieController {
    private final FavoriteMovieService favoriteMovieService;


    public FavoriteMovieController(FavoriteMovieService favoriteMovieService) {
        this.favoriteMovieService = favoriteMovieService;
    }

    // List phim yêu thích

    @Operation(summary = "Get list favorite movies", description = "API get all favourite movies")
    @GetMapping("/")
    public ResponseEntity<List<MovieDTO>> getFavoriteMoviesByUser(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt) {
        CustomUserDetails user = null;
        if (jwt != null) {
            user = new CustomUserDetails();
            Long userId = jwt.getClaim("userId");
            user.setId(userId);
        }
        assert user != null;
        List<MovieDTO> movies = favoriteMovieService.getAllFavouriteMovies(user);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    // API Like phim
    @Operation(summary = "Like movie", description = "API like movie")
    @PostMapping("/like/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> likeMovie(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
                                                           @PathVariable Long movieId) {
        CustomUserDetails user = null;
        if (jwt != null) {
            user = new CustomUserDetails();
            Long userId = jwt.getClaim("userId");
            user.setId(userId);
        }
        assert user != null;
        Boolean liked = favoriteMovieService.likeMovie(user, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage(liked ? "Like thành công" : "Đã like trước đó");
        res.setData(liked); // Tùy chọn

        return ResponseEntity.ok(res);
    }

    // API Unlike phim
    @Operation(summary = "Unlike movie", description = "API unlike movie")
    @DeleteMapping("/unlike/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> unlikeMovie(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
                                                             @PathVariable Long movieId) {

        CustomUserDetails user = null;
        if (jwt != null) {
            user = new CustomUserDetails();
            Long userId = jwt.getClaim("userId");
            user.setId(userId);
        }
        assert user != null;
        Boolean unliked = favoriteMovieService.unlikeMovie(user, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage(unliked ? " Hủy Like thành công" : "Chưa like trước đó");
        res.setData(unliked);

        return ResponseEntity.ok(res);

    }

    // API Check trạng thái Like
    @Operation(summary = "Check like movie", description = "API check like movie")
    @GetMapping("/check/{movieId}")
    public ResponseEntity<RestResponse<Boolean>> checkLikeStatus(@AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
                                                                 @PathVariable Long movieId) {
        CustomUserDetails user = null;
        if (jwt != null) {
            user = new CustomUserDetails();
            Long userId = jwt.getClaim("userId");
            user.setId(userId);
        }
        assert user != null;
        Boolean check = favoriteMovieService.checkIfUserLikedMovie(user, movieId);
        RestResponse<Boolean> res = new RestResponse<>();
        res.setStatusCode(200);
        res.setMessage(check ? "Đã like" : "Chưa like");
        res.setData(check);

        return ResponseEntity.ok(res);
    }
}
