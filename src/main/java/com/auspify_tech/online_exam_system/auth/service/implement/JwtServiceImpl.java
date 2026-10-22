package com.auspify_tech.online_exam_system.auth.service.implement;

import com.auspify_tech.online_exam_system.auth.entity.RefreshTokenEntity;
import com.auspify_tech.online_exam_system.auth.entity.UserEntity;
import com.auspify_tech.online_exam_system.auth.repository.RefreshTokenRepository;
import com.auspify_tech.online_exam_system.auth.service.JwtService;
import com.auspify_tech.online_exam_system.common.jwt.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtServiceImpl implements JwtService {
    final RefreshTokenRepository refreshTokenRepository;
    final JwtProperties jwtProperties;
    Key accessTokenKey;

    @PostConstruct
    void init() {
        String secret = jwtProperties.accessToken().secret();

        if (Objects.isNull(secret) || secret.length() < 32) {
            throw new IllegalStateException("JWT access token secret must be at least 256 bits!");
        }

        this.accessTokenKey = Keys.hmacShaKeyFor(
                jwtProperties.accessToken().secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String generateAccessToken(UserEntity userEntity) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.accessToken().expirationMinutes(),
                ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(userEntity.getId())
                .claim("email", userEntity.getEmail())
                .claim("role", userEntity.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(accessTokenKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserEntity userEntity) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.refreshToken().expirationDays(),
                ChronoUnit.DAYS);

        String tokenId = UUID.randomUUID().toString();
        String tokenValue = generateSecureRandomToken(64);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(userEntity)
                .tokenId(tokenId)
                .tokenHash(hashToken(tokenValue))
                .expiryDate(expiry)
                .revokedAt(null)
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenValue;
    }

    private String generateSecureRandomToken(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < length; i++) {
            int idx = secureRandom.nextInt(chars.length());
            stringBuilder.append(chars.charAt(idx));
        }

        return stringBuilder.toString();
    }

    @Override
    public String hashToken(String tokenValue) {
        return DigestUtils.sha256Hex(tokenValue);
    }

    @Override
    public Key getSigningKey() {
        return this.accessTokenKey;
    }
}
