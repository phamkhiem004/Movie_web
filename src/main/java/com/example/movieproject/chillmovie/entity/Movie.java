package com.example.movieproject.chillmovie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Column(name = "poster_url")
    private String posterUrl;

    private Integer duration; // phút

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String country;

    private String language;

    @Column(name = "age_limit")
    private Integer ageLimit;

}
