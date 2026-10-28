package com.auspify_tech.online_exam_system.exam.mapper;

import com.auspify_tech.online_exam_system.exam.dto.request.CreateOptionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.CreateQuestionRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.OptionResponseDto;
import com.auspify_tech.online_exam_system.exam.dto.response.QuestionResponseDto;
import com.auspify_tech.online_exam_system.exam.entity.OptionEntity;
import com.auspify_tech.online_exam_system.exam.entity.QuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    QuestionEntity mapRequestToEntity(CreateQuestionRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OptionEntity mapOptionRequestToOptionEntity(CreateOptionRequestDto optionDto);

    OptionResponseDto mapOptionEntityToOptionResponseDto(OptionEntity option);

    List<OptionResponseDto> mapOptionEntitiesToOptionResponseDtos(List<OptionEntity> options);

    @Mapping(source = "question.id", target = "id")
    @Mapping(source = "question.exam.id", target = "examId")
    @Mapping(source = "question.content", target = "content")
    @Mapping(source = "question.points", target = "points")
    @Mapping(source = "question.questionType", target = "questionType")
    @Mapping(source = "options", target = "options")
    QuestionResponseDto mapEntitiesToResponseDto(QuestionEntity question, List<OptionEntity> options);

}
