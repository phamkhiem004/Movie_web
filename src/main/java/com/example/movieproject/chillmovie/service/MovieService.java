package com.example.movieproject.chillmovie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.respository.MovieRepository;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
