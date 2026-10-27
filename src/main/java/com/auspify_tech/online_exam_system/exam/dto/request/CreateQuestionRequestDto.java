package com.auspify_tech.online_exam_system.exam.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateQuestionRequestDto(@NotBlank(message = "Question text cannot be blank")
                                       String text,
                                       @NotNull(message = "Points are required")
                                       @Min(value = 1, message = "Points must be at least 1")
                                       Integer points,
                                       @NotEmpty(message = "A question must have at least one option")
                                       List<@Valid CreateOptionRequestDto> options) {
}
