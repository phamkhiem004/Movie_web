package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.RedisToken;
import com.example.movieproject.chillmovie.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
}
