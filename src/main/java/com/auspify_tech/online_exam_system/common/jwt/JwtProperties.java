package com.auspify_tech.online_exam_system.common.jwt;

public record JwtProperties(AccessTokenProperties accessToken,
                            RefreshTokenProperties refreshToken) {

}
