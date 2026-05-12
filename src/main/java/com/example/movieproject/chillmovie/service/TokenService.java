package com.example.movieproject.chillmovie.service;


import com.example.movieproject.chillmovie.entity.Token;
import com.example.movieproject.chillmovie.respository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public void save(Token token) {
        // Revoke tất cả token cũ của user trước khi lưu mới
        // Tránh tích lũy token cũ trong DB
        tokenRepository.findAllByUserId(token.getUser().getId())
                .forEach(t -> {
                    t.setRevoked(true);
                    t.setExpired(true);
                });
        tokenRepository.save(token);
    }

    public Token findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken).orElse(null);
    }

    public void revokeByUserId(Long userId) {
        tokenRepository.findAllByUserId(userId)
                .forEach(t -> {
                    t.setRevoked(true);
                    t.setExpired(true);
                });
    }
}
