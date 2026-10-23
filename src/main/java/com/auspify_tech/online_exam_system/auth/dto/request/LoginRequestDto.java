package com.auspify_tech.online_exam_system.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(@NotBlank(message = "Email cannot be empty!")
                              @Email(message = "Enter valid mail format!")
                              String email,
                              @NotBlank(message = "Password cannot be empty!")
                              @Size(min = 8, message = "Password must be at least 8 characters long!")
                              String password) {

}
