package com.auspify_tech.online_exam_system.exam.service;

import com.auspify_tech.online_exam_system.exam.dto.request.CreateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.UpdateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.ExamResponseDto;
import com.auspify_tech.online_exam_system.exam.model.ExamStatus;
import org.springframework.data.domain.Page;

public interface ExamService {

    Page<ExamResponseDto> getAllExams(int page, int size);

    Page<ExamResponseDto> getActiveExams(int page, int size);

    ExamResponseDto getExamById(String id);

    ExamResponseDto createExam(CreateExamRequestDto createExamRequestDto);

    ExamResponseDto updateExam(String id, UpdateExamRequestDto updateExamRequestDto);

    void changeExamStatus(String id, ExamStatus status);

    void deleteExam(String id);

}
