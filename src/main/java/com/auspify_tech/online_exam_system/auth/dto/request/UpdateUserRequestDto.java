package com.auspify_tech.online_exam_system.auth.dto.request;

import com.auspify_tech.online_exam_system.auth.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(@Size(max = 30, message = "First name must not exceed 30 characters")
                                   String firstName,
                                   @Size(max = 30, message = "Last name must not exceed 30 characters")
                                   String lastName,
                                   String phoneNumber,
                                   @Email(message = "Invalid email format")
                                   String email,
                                   UserRole role) {

}
