package com.example.movieproject.chillmovie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MovieType type;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private Set<MovieActor> movieActors = new HashSet<>();

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private Set<MovieGenre> movieGenres = new HashSet<>();
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Episode> episodes = new ArrayList<>();

}
