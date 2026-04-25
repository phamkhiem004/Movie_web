package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.DTO.GenreDTO;
import com.example.movieproject.chillmovie.entity.Genre;
import com.example.movieproject.chillmovie.respository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreDTO> findAll() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream().map(m ->{
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setId(m.getId());
            genreDTO.setName(m.getName());
            return genreDTO;
        }).toList();

    }

}
