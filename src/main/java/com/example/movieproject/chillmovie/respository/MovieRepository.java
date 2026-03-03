package com.example.movieproject.chillmovie.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieproject.chillmovie.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
