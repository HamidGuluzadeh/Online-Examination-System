package com.auspify_tech.online_exam_system.exam.dto.response;

import java.util.List;

public record QuestionResponseDto(String id,
                                  String examId,
                                  String text,
                                  Integer points,
                                  List<OptionResponseDto> options) {

}
