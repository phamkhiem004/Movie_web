package com.example.movieproject.chillmovie.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(name = "access_token")
    private String accessToken;

    @Lob
    @Column(name = "refresh_token")
    private String refreshToken;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "expired", nullable = false)
    private Boolean expired = false;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "revoked", nullable = false)
    private Boolean revoked = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

}