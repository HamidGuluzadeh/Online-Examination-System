package com.auspify_tech.online_exam_system.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDto(@NotBlank(message = "New password cannot be blank")
                                      @Size(min = 8, message = "Password must be at least 8 characters long")
                                      String newPassword,
                                      @NotBlank(message = "Password cannot be blank")
                                      @Size(min = 8, message = "Password must be at least 8 characters long")
                                      String confirmPassword) {

}
