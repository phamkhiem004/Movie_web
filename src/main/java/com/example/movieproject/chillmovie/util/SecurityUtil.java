package com.example.movieproject.chillmovie.util;

import com.example.movieproject.chillmovie.DTO.CustomUserDetails;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class SecurityUtil {

    private final JwtEncoder jwtEncoder;
    public static final MacAlgorithm JWT_AlGORITHM = MacAlgorithm.HS512;

    @Value("${khiem.jwt.base64-secret}")
    private String jwtKey;

    @Value("${khiem.jwt.token-validity-in-seconds}")
    private long jwtExpiration;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }


    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plus(jwtExpiration, ChronoUnit.SECONDS);

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Long userId = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) principal).getId();
        }




        // Sau đó mới đưa vào claim
        assert userId != null;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("userId", userId)
                .claim("roles", roles)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_AlGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();


    }

    public String extractUsername(String token) {
        try {
            // Dùng lại NimbusJwtDecoder — cùng secret key với encoder
            NimbusJwtDecoder decoder = NimbusJwtDecoder
                    .withSecretKey(getSecretKey())
                    .macAlgorithm(JWT_AlGORITHM)
                    .build();

            // decode() tự động throw exception nếu:
            // - Sai chữ ký
            // - Token hết hạn
            // - Token bị malformed
            return decoder.decode(token).getSubject();

        } catch (JwtException e) {
            // Throw lên để JwtAuthFilter bắt và trả 401
            throw new JwtException("Token không hợp lệ hoặc đã hết hạn: "
                    + e.getMessage());
        }
    }

    // ── THÊM MỚI: kiểm tra token hết hạn chưa ──────────────
    public boolean isTokenExpired(String token) {
        try {
            NimbusJwtDecoder decoder = NimbusJwtDecoder
                    .withSecretKey(getSecretKey())
                    .macAlgorithm(JWT_AlGORITHM)
                    .build();

            Instant expiresAt = decoder.decode(token).getExpiresAt();
            return expiresAt != null && expiresAt.isBefore(Instant.now());

        } catch (JwtException e) {
            return true; // Không decode được → coi như hết hạn
        }
    }

    // ── THÊM MỚI: lấy claim bất kỳ nếu cần sau này ─────────
    public <T> T extractClaim(String token, String claimName, Class<T> type) {
        try {
            NimbusJwtDecoder decoder = NimbusJwtDecoder
                    .withSecretKey(getSecretKey())
                    .macAlgorithm(JWT_AlGORITHM)
                    .build();

            return decoder.decode(token).getClaim(claimName);

        } catch (JwtException e) {
            throw new JwtException("Không thể đọc claim: " + e.getMessage());
        }
    }

    // ── Helper dùng chung (private) ──────────────────────────
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(
                keyBytes, 0, keyBytes.length,
                JWT_AlGORITHM.getName()
        );
    }
}

