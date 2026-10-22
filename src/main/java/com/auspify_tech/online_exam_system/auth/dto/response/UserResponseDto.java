package com.auspify_tech.online_exam_system.auth.dto.response;

import com.auspify_tech.online_exam_system.auth.model.UserRole;
import com.auspify_tech.online_exam_system.auth.model.UserStatus;

import java.time.Instant;

public record UserResponseDto(String id,
                              String username,
                              String firstName,
                              String lastName,
                              String phoneNumber,
                              String email,
                              UserRole role,
                              UserStatus status,
                              Instant createdAt,
                              Instant updatedAt) {

}
