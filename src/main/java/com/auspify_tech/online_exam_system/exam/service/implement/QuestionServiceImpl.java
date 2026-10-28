package com.auspify_tech.online_exam_system.exam.service.implement;

import com.auspify_tech.online_exam_system.common.exception.BadRequestException;
import com.auspify_tech.online_exam_system.common.exception.ResourceNotFoundException;
import com.auspify_tech.online_exam_system.exam.dto.request.CreateOptionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.CreateQuestionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.UpdateQuestionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.QuestionResponseDto;
import com.auspify_tech.online_exam_system.exam.entity.ExamEntity;
import com.auspify_tech.online_exam_system.exam.entity.OptionEntity;
import com.auspify_tech.online_exam_system.exam.entity.QuestionEntity;
import com.auspify_tech.online_exam_system.exam.mapper.QuestionMapper;
import com.auspify_tech.online_exam_system.exam.model.ExamStatus;
import com.auspify_tech.online_exam_system.exam.model.QuestionType;
import com.auspify_tech.online_exam_system.exam.repository.ExamRepository;
import com.auspify_tech.online_exam_system.exam.repository.OptionRepository;
import com.auspify_tech.online_exam_system.exam.repository.QuestionRepository;
import com.auspify_tech.online_exam_system.exam.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    OptionRepository optionRepository;
    ExamRepository examRepository;
    QuestionMapper questionMapper;

    @Override
    public List<QuestionResponseDto> getQuestionsByExamId(String examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }

        return questionRepository.findByExamId(examId).stream()
                .map(question -> {
                    List<OptionEntity> options = optionRepository.findByQuestionId(question.getId());
                    return questionMapper.mapEntitiesToResponseDto(question, options);
                })
                .toList();
    }

    @Override
    public QuestionResponseDto getQuestionById(String questionId) {
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        List<OptionEntity> options = optionRepository.findByQuestionId(questionId);

        return questionMapper.mapEntitiesToResponseDto(question, options);
    }

    @Override
    @Transactional
    public QuestionResponseDto addQuestionToExam(String examId, CreateQuestionRequestDto requestDto) {
        ExamEntity exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found!"));

        if (exam.getStatus() != ExamStatus.DRAFT) {
            throw new BadRequestException("Questions can only be added to exams in DRAFT status!");
        }

        validateQuestionOptions(requestDto.questionType(), requestDto.options());

        QuestionEntity questionEntity = questionMapper.mapRequestToEntity(requestDto);
        questionEntity.setExam(exam);
        questionEntity.setQuestionType(requestDto.questionType());
        questionEntity.setCreatedAt(Instant.now());

        QuestionEntity savedQuestionEntity = questionRepository.save(questionEntity);

        List<OptionEntity> optionEntities = requestDto.options().stream()
                .map(optionDto -> {
                    OptionEntity optionEntity = questionMapper.mapOptionRequestToOptionEntity(optionDto);
                    optionEntity.setQuestion(savedQuestionEntity);
                    return optionEntity;
                })
                .toList();

        List<OptionEntity> savedOptions = optionRepository.saveAll(optionEntities);

        return questionMapper.mapEntitiesToResponseDto(savedQuestionEntity, savedOptions);
    }

    @Override
    @Transactional
    public QuestionResponseDto updateQuestion(String questionId, UpdateQuestionRequestDto requestDto) {
        QuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        if (question.getExam().getStatus() != ExamStatus.DRAFT) {
            throw new BadRequestException("Questions can only be updated for exams in DRAFT status!");
        }

        if (requestDto.content() != null) {
            question.setContent(requestDto.content());
        }
        if (requestDto.points() != null) {
            question.setPoints(requestDto.points());
        }

        QuestionType effectiveType = requestDto.questionType() != null
                ? requestDto.questionType()
                : question.getQuestionType();

        List<OptionEntity> currentOptions;

        if (requestDto.options() != null && !requestDto.options().isEmpty()) {

            validateQuestionOptions(effectiveType, requestDto.options());

            optionRepository.deleteByQuestionId(questionId);

            List<OptionEntity> newOptions = requestDto.options().stream()
                    .map(optionDto -> {
                        OptionEntity option = questionMapper.mapOptionRequestToOptionEntity(optionDto);
                        option.setQuestion(question);
                        return option;
                    })
                    .toList();

            currentOptions = optionRepository.saveAll(newOptions);
            question.setQuestionType(effectiveType);

        } else {
            currentOptions = optionRepository.findByQuestionId(questionId);

            if (requestDto.questionType() != null && requestDto.questionType() != question.getQuestionType()) {
                question.setQuestionType(effectiveType);
            }
        }

        QuestionEntity updatedQuestion = questionRepository.save(question);

        return questionMapper.mapEntitiesToResponseDto(updatedQuestion, currentOptions);
    }

    @Override
    @Transactional
    public void deleteQuestion(String questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found!");
        }

        questionRepository.deleteById(questionId);
    }

    private void validateQuestionOptions(QuestionType type, List<CreateOptionRequestDto> options) {
        if (options.size() < 2) {
            throw new BadRequestException("A question must contain at least 2 options!");
        }

        long correctCount = options.stream()
                .filter(option -> Boolean.TRUE.equals(option.isCorrect()))
                .count();

        if (correctCount == 0) {
            throw new BadRequestException("A question must have at least one correct option!");
        }

        switch (type) {
            case SINGLE_CHOICE -> {
                if (correctCount > 1) {
                    throw new BadRequestException("SINGLE_CHOICE questions can only have exactly 1 correct option!");
                }
            }
            case MULTIPLE_CHOICE -> {
                if (correctCount < 2) {
                    throw new BadRequestException("MULTIPLE_CHOICE questions should ideally have more than 1 correct option!");
                }
            }
            case TRUE_FALSE -> {
                if (options.size() != 2) {
                    throw new BadRequestException("TRUE_FALSE questions must have exactly 2 options!");
                }
                if (correctCount != 1) {
                    throw new BadRequestException("TRUE_FALSE questions must have exactly 1 correct option!");
                }
            }
        }
    }
}
