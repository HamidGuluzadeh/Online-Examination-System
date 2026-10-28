package com.auspify_tech.online_exam_system.exam.service;

import com.auspify_tech.online_exam_system.exam.dto.request.CreateQuestionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.UpdateQuestionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.QuestionResponseDto;

import java.util.List;

public interface QuestionService {

    List<QuestionResponseDto> getQuestionsByExamId(String examId);

    QuestionResponseDto getQuestionById(String questionId);

    QuestionResponseDto addQuestionToExam(String examId, CreateQuestionRequestDto requestDto);

    QuestionResponseDto updateQuestion(String questionId, UpdateQuestionRequestDto requestDto);

    void deleteQuestion(String questionId);

}
