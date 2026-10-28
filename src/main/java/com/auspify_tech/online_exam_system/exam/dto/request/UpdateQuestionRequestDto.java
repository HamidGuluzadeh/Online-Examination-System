package com.auspify_tech.online_exam_system.exam.dto.request;

import com.auspify_tech.online_exam_system.exam.model.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateQuestionRequestDto(String content,
                                       @Min(value = 1, message = "Points must be at least 1")
                                       Integer points,
                                       QuestionType questionType,
                                       @NotEmpty(message = "A question must have options")
                                       List<@Valid CreateOptionRequestDto> options) {

}
