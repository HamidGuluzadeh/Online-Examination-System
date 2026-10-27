package com.auspify_tech.online_exam_system.exam.dto.response;

import java.time.Instant;

public record ExamResponseDto(String id,
                              String title,
                              String description,
                              Integer durationMinutes,
                              Integer passingScore,
                              Instant startTime,
                              Instant endTime,
                              String status,
                              String createdBy,
                              String updatedBy,
                              Instant createdAt,
                              Instant updatedAt) {

}
