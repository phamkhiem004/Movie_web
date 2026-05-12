package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

    Optional<Token> findByAccessToken(String accessToken);
    List<Token> findAllByUserId(Long userId);
}
