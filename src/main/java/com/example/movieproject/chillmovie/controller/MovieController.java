package com.example.movieproject.chillmovie.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.service.MovieService;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return movies;
    }

}
