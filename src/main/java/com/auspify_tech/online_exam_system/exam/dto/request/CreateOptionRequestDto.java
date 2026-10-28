package com.auspify_tech.online_exam_system.exam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOptionRequestDto(@NotBlank(message = "Option text cannot be blank")
                                     String content,
                                     @NotNull(message = "Correctness flag (isCorrect) must be provided")
                                     Boolean isCorrect) {

}
