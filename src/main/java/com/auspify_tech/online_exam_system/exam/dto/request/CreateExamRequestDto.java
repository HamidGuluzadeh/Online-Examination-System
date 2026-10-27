package com.auspify_tech.online_exam_system.exam.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateExamRequestDto(@NotBlank(message = "Title cannot be blank")
                                   String title,
                                   String description,
                                   @NotNull(message = "Duration is required")
                                   @Min(value = 5, message = "Duration must be at least 5 minutes")
                                   Integer durationMinutes,
                                   @NotNull(message = "Passing score is required")
                                   @Min(value = 1, message = "Passing score must be at least 1")
                                   Integer passingScore,
                                   @NotNull(message = "Start time is required")
                                   @Future(message = "Start time must be in the future")
                                   Instant startTime,
                                   @NotNull(message = "End time is required")
                                   @Future(message = "End time must be in the future")
                                   Instant endTime) {

}
