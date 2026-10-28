package com.auspify_tech.online_exam_system.exam.controller;

import com.auspify_tech.online_exam_system.common.dto.SuccessDto;
import com.auspify_tech.online_exam_system.common.model.SuccessStatus;
import com.auspify_tech.online_exam_system.exam.dto.request.CreateQuestionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.UpdateQuestionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.QuestionResponseDto;
import com.auspify_tech.online_exam_system.exam.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Question Controller", description = "Question Management")
public class QuestionController {
    QuestionService questionService;

    @GetMapping("/exam/{examId}")
    @Operation(summary = "Get questions by Exam ID")
    public ResponseEntity<SuccessDto<List<QuestionResponseDto>>> getQuestionsByExamId(@PathVariable String examId) {
        List<QuestionResponseDto> response = questionService.getQuestionsByExamId(examId);
        SuccessDto<List<QuestionResponseDto>> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "Get question by ID")
    public ResponseEntity<SuccessDto<QuestionResponseDto>> getQuestionById(@PathVariable String questionId) {
        QuestionResponseDto response = questionService.getQuestionById(questionId);
        SuccessDto<QuestionResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PostMapping("/exam/{examId}")
    @Operation(summary = "Add question to an exam by Exam ID")
    public ResponseEntity<SuccessDto<QuestionResponseDto>> addQuestionToExam(@PathVariable String examId,
                                                                             @RequestBody @Valid CreateQuestionRequestDto requestDto) {
        QuestionResponseDto response = questionService.addQuestionToExam(examId, requestDto);
        SuccessDto<QuestionResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "Update question by ID")
    public ResponseEntity<SuccessDto<QuestionResponseDto>> updateQuestion(@PathVariable String questionId,
                                                                          @RequestBody @Valid UpdateQuestionRequestDto requestDto) {
        QuestionResponseDto response = questionService.updateQuestion(questionId, requestDto);
        SuccessDto<QuestionResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}")
    @Operation(summary = "Delete question by ID")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
