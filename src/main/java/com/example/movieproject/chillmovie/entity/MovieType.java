package com.example.movieproject.chillmovie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MovieType {
    @JsonProperty("single")
    SINGLE, // phim lẻ
    @JsonProperty("series")
    SERIES  // phim bộ
}
