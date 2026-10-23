package com.auspify_tech.online_exam_system.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthResponseDto(String accessToken,
                              String refreshToken) {

}
