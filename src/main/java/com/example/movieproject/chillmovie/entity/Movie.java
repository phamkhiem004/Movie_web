package com.example.movieproject.chillmovie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Entity
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
    private MovieStatus status = MovieStatus.NOW_SHOWING;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // ================= AUDIT =================

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ================= CONSTRUCTOR =================

    public Movie() {
    }

    // ================= GETTER & SETTER =================

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // (Bạn có thể generate getter/setter bằng Lombok nếu muốn)

}
