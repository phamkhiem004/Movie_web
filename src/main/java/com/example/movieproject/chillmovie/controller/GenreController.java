package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.GenreDTO;
import com.example.movieproject.chillmovie.entity.Genre;
import com.example.movieproject.chillmovie.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreController {

    private final GenreService genreService;
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public ResponseEntity<List<GenreDTO>> findAll() {
        List<GenreDTO> genreDTOS = genreService.findAll();
        return ResponseEntity.ok().body(genreDTOS);
    }
}
