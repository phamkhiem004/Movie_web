package com.example.movieproject.chillmovie.util;

import com.example.movieproject.chillmovie.DTO.UserDetailsCustom;
import com.example.movieproject.chillmovie.entity.RedisToken;
import com.example.movieproject.chillmovie.entity.Token;
import com.example.movieproject.chillmovie.service.RedisTokenService;
import com.example.movieproject.chillmovie.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecurityUtil securityUtil;
    private final RedisTokenService redisTokenService;
    private final TokenService tokenService;
    private final UserDetailsCustom userDetailsCustom;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username;

        try {
            username = securityUtil.extractUsername(jwt);
        } catch (Exception e) {
            // ✅ Token lỗi → KHÔNG sendError, để Spring Security tự quyết
            // permitAll  → vẫn 200
            // authenticated() → Spring Security tự trả 401
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            boolean isValid = false;

            try {
                RedisToken redisToken = redisTokenService.findById(username);

                if (redisToken != null) {
                    // ✅ Không khớp → isValid = false, KHÔNG sendError
                    isValid = jwt.equals(redisToken.getAccessToken());

                } else {
                    Token dbToken = tokenService.findByAccessToken(jwt);

                    if (dbToken != null
                            && !dbToken.getExpired()
                            && !dbToken.getRevoked()) {
                        isValid = true;
                        try {
                            redisTokenService.save(RedisToken.builder()
                                    .id(username)
                                    .accessToken(dbToken.getAccessToken())
                                    .refreshToken(dbToken.getRefreshToken())
                                    .build());
                        } catch (Exception redisEx) {
                            // Redis lỗi khi re-cache → không sao
                            logger.warn("Không thể re-cache Redis: " + redisEx.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                // Redis/DB lỗi → isValid = false, KHÔNG crash filter
                logger.error("Lỗi validate token: " + e.getMessage());
            }

            // ✅ Chỉ set SecurityContext nếu valid
            // Không valid → SecurityContext trống → Spring Security tự quyết
            if (isValid) {
                UserDetails userDetails = userDetailsCustom.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ✅ Luôn luôn ở cuối — không có return nào chặn đến đây
        filterChain.doFilter(request, response);
    }
}