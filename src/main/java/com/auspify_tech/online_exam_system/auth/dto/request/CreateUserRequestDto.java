package com.auspify_tech.online_exam_system.auth.dto.request;

import com.auspify_tech.online_exam_system.auth.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(@NotBlank(message = "Username cannot be blank")
                                   @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
                                   String username,
                                   @NotBlank(message = "First name cannot be blank")
                                   @Size(max = 30, message = "First name must not exceed 30 characters")
                                   String firstName,
                                   @NotBlank(message = "Last name cannot be blank")
                                   @Size(max = 30, message = "Last name must not exceed 30 characters")
                                   String lastName,
                                   @NotBlank(message = "Password cannot be blank")
                                   @Size(min = 8, message = "Password must be at least 8 characters long")
                                   String password,
                                   @NotBlank(message = "Phone number cannot be blank")
                                   String phoneNumber,
                                   @Email(message = "Invalid email format")
                                   String email,
                                   @NotNull(message = "Role cannot be null")
                                   UserRole role) {

}
