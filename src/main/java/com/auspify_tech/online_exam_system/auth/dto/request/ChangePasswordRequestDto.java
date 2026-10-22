package com.auspify_tech.online_exam_system.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(@NotBlank(message = "Old password cannot be blank")
                                       String oldPassword,
                                       @NotBlank(message = "New password cannot be blank")
                                       @Size(min = 8, message = "New password must be at least 8 characters long")
                                       String newPassword,
                                       @NotBlank(message = "Confirm password cannot be blank")
                                       String confirmPassword) {

}
