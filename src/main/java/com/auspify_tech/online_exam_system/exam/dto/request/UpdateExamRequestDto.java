package com.auspify_tech.online_exam_system.exam.dto.request;

import jakarta.validation.constraints.Min;

import java.time.Instant;

public record UpdateExamRequestDto(String title,
                                   String description,
                                   @Min(value = 5, message = "Duration must be at least 5 minutes")
                                   Integer durationMinutes,
                                   @Min(value = 1, message = "Passing score must be at least 1")
                                   Integer passingScore,
                                   Instant startTime,
                                   Instant endTime) {

}
