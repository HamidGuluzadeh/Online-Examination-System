package com.auspify_tech.online_exam_system.auth.service.implement;

import com.auspify_tech.online_exam_system.auth.dto.request.CreateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.LoginRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.RefreshRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.AuthResponseDto;
import com.auspify_tech.online_exam_system.auth.entity.RefreshTokenEntity;
import com.auspify_tech.online_exam_system.auth.entity.UserEntity;
import com.auspify_tech.online_exam_system.auth.model.UserStatus;
import com.auspify_tech.online_exam_system.auth.repository.RefreshTokenRepository;
import com.auspify_tech.online_exam_system.auth.repository.UserRepository;
import com.auspify_tech.online_exam_system.auth.service.AuthService;
import com.auspify_tech.online_exam_system.auth.service.JwtService;
import com.auspify_tech.online_exam_system.common.exception.BadRequestException;
import com.auspify_tech.online_exam_system.common.exception.ResourceAlreadyExistsException;
import com.auspify_tech.online_exam_system.common.exception.ResourceNotFoundException;
import com.auspify_tech.online_exam_system.common.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        UserEntity userEntity = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (!passwordEncoder.matches(loginRequestDto.password(), userEntity.getPassword())) {
            throw new UnauthorizedException("Password is incorrect!");
        }

        if (!userEntity.getStatus().equals(UserStatus.ACTIVE)) {
            throw new BadRequestException("Account is not in ACTIVE status!");
        }

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDto refresh(RefreshRequestDto refreshRequestDto) {
        String hashedToken = jwtService.hashToken(refreshRequestDto.refreshToken());

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenHash(hashedToken)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token!"));

        if (refreshTokenEntity.getRevokedAt() != null || refreshTokenEntity.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Invalid Refresh token!");
        }

        UserEntity userEntity = refreshTokenEntity.getUser();

        refreshTokenEntity.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshTokenEntity);

        String newAccessToken = jwtService.generateAccessToken(userEntity);
        String newRefreshToken = jwtService.generateRefreshToken(userEntity);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    @Transactional
    public void logout() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<RefreshTokenEntity> validTokens =
                refreshTokenRepository.findAllByUserIdAndRevokedAtIsNull(userId);

        if (!validTokens.isEmpty()) {
            validTokens.forEach(refreshTokenEntity ->
                    refreshTokenEntity.setRevokedAt(Instant.now()));
            refreshTokenRepository.saveAll(validTokens);
        }
    }
}
