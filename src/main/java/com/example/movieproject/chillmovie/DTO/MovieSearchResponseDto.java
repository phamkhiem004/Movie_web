package com.example.movieproject.chillmovie.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieSearchResponseDto {
    private List<MovieSearchItemDto> items;
    private int page;
    private int size;
    private long total;

    public MovieSearchResponseDto(List<MovieSearchItemDto> items, int page, int size, long total) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
    }

}
