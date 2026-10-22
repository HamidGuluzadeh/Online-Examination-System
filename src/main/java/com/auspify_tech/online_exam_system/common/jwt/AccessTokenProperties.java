package com.auspify_tech.online_exam_system.common.jwt;

public record AccessTokenProperties(String secret,
                                    Integer expirationMinutes) {

}
