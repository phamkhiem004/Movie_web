package com.example.movieproject.chillmovie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("blocked")
    BLOCKED
}
