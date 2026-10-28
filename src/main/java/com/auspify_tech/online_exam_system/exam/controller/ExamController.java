package com.auspify_tech.online_exam_system.exam.controller;

import com.auspify_tech.online_exam_system.common.dto.SuccessDto;
import com.auspify_tech.online_exam_system.common.model.SuccessStatus;
import com.auspify_tech.online_exam_system.exam.dto.request.CreateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.UpdateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.ExamResponseDto;
import com.auspify_tech.online_exam_system.exam.model.ExamStatus;
import com.auspify_tech.online_exam_system.exam.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Exam Controller", description = "Exam Management")
public class ExamController {
    ExamService examService;

    @GetMapping("/all")
    @Operation(summary = "Get all exams")
    public ResponseEntity<SuccessDto<Page<ExamResponseDto>>> getAllExams(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size) {
        Page<ExamResponseDto> response = examService.getAllExams(page, size);
        SuccessDto<Page<ExamResponseDto>> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active exams")
    public ResponseEntity<SuccessDto<Page<ExamResponseDto>>> getActiveExams(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "20") int size) {
        Page<ExamResponseDto> response = examService.getActiveExams(page, size);
        SuccessDto<Page<ExamResponseDto>> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get exam by ID")
    public ResponseEntity<SuccessDto<ExamResponseDto>> getExamById(@PathVariable String id) {
        ExamResponseDto response = examService.getExamById(id);
        SuccessDto<ExamResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create exam")
    public ResponseEntity<SuccessDto<ExamResponseDto>> createExam(@RequestBody @Valid CreateExamRequestDto createExamRequestDto) {
        ExamResponseDto response = examService.createExam(createExamRequestDto);
        SuccessDto<ExamResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update exam by ID")
    public ResponseEntity<SuccessDto<ExamResponseDto>> updateExam(@PathVariable String id,
                                                                  @RequestBody @Valid UpdateExamRequestDto updateExamRequestDto) {
        ExamResponseDto response = examService.updateExam(id, updateExamRequestDto);
        SuccessDto<ExamResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeExamStatus(@PathVariable String id, @RequestParam ExamStatus status) {
        examService.changeExamStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete exam by ID")
    public ResponseEntity<Void> deleteExam(@PathVariable String id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}
