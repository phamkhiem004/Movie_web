package com.example.movieproject.chillmovie.service;


import com.example.movieproject.chillmovie.entity.RedisToken;
import com.example.movieproject.chillmovie.respository.RedisTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {

    private final RedisTokenRepository redisTokenRepository;

    public RedisTokenService(RedisTokenRepository redisTokenRepository) {
        this.redisTokenRepository = redisTokenRepository;
    }

    public String save(RedisToken token){
        RedisToken rs = redisTokenRepository.save(token);
        return rs.getId();
    }

    public void delete(String id){
        redisTokenRepository.deleteById(id);
    }

    public RedisToken findById(String username) {
        return redisTokenRepository.findById(username).orElse(null);
    }

    public boolean isExists(String username) {
        return redisTokenRepository.existsById(username);
    }
}
