package com.auspify_tech.online_exam_system.exam.service.implement;

import com.auspify_tech.online_exam_system.common.exception.ResourceAlreadyExistsException;
import com.auspify_tech.online_exam_system.common.exception.ResourceNotFoundException;
import com.auspify_tech.online_exam_system.exam.dto.request.CreateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.UpdateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.ExamResponseDto;
import com.auspify_tech.online_exam_system.exam.entity.ExamEntity;
import com.auspify_tech.online_exam_system.exam.mapper.ExamMapper;
import com.auspify_tech.online_exam_system.exam.model.ExamStatus;
import com.auspify_tech.online_exam_system.exam.repository.ExamRepository;
import com.auspify_tech.online_exam_system.exam.service.ExamService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamServiceImpl implements ExamService {
    ExamRepository examRepository;
    ExamMapper examMapper;

    @Override
    public Page<ExamResponseDto> getAllExams(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ExamEntity> examEntities = examRepository.findAll(pageable);

        return examEntities.map(examMapper::mapEntityToResponse);
    }

    @Override
    public Page<ExamResponseDto> getActiveExams(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ExamEntity> examEntities = examRepository.findAllByStatus(ExamStatus.ACTIVE, pageable);

        return examEntities.map(examMapper::mapEntityToResponse);
    }

    @Override
    public ExamResponseDto getExamById(String id) {
        ExamEntity examEntity = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found!"));

        return examMapper.mapEntityToResponse(examEntity);
    }

    @Override
    @Transactional
    public ExamResponseDto createExam(CreateExamRequestDto createExamRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        ExamEntity examEntity = examMapper.mapCreateRequestToEntity(createExamRequestDto);
        examEntity.setStatus(ExamStatus.ACTIVE);
        examEntity.setCreatedAt(Instant.now());
        examEntity.setCreatedBy(userId);

        ExamEntity savedExamEntity = examRepository.save(examEntity);

        return examMapper.mapEntityToResponse(savedExamEntity);
    }

    @Override
    @Transactional
    public ExamResponseDto updateExam(String id, UpdateExamRequestDto updateExamRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        ExamEntity examEntity = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found!"));

        examMapper.mapUpdateRequestToEntity(updateExamRequestDto, examEntity);
        examEntity.setUpdatedAt(Instant.now());
        examEntity.setUpdatedBy(userId);

        ExamEntity savedExamEntity = examRepository.save(examEntity);

        return examMapper.mapEntityToResponse(savedExamEntity);
    }

    @Override
    @Transactional
    public void changeExamStatus(String id, ExamStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        ExamEntity examEntity = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found!"));

        examEntity.setStatus(status);
        examEntity.setUpdatedAt(Instant.now());
        examEntity.setUpdatedBy(userId);
    }

    @Override
    @Transactional
    public void deleteExam(String id) {
        if (!examRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exam not found!");
        }

        examRepository.deleteById(id);
    }
}
