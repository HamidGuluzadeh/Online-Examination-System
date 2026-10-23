package com.auspify_tech.online_exam_system.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDto(@NotBlank(message = "Refresh token cannot be empty!")
                                String refreshToken) {

}
