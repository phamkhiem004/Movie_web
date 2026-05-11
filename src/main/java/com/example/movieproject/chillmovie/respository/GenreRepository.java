package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {

    Optional<Genre> findByGenreIdAndIsDeletedFalse(Integer genreId);

    List<Genre> findAllByIsDeletedFalse();
}
