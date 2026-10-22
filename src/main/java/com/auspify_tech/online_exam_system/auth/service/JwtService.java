package com.auspify_tech.online_exam_system.auth.service;

import com.auspify_tech.online_exam_system.auth.entity.UserEntity;

import java.security.Key;

public interface JwtService {

    String generateAccessToken(UserEntity userEntity);

    String generateRefreshToken(UserEntity userEntity);

    String hashToken(String tokenValue);

    Key getSigningKey();

}
