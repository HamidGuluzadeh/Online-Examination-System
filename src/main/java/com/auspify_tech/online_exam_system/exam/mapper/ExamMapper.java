package com.auspify_tech.online_exam_system.exam.mapper;

import com.auspify_tech.online_exam_system.exam.dto.request.CreateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.request.UpdateExamRequestDto;
import com.auspify_tech.online_exam_system.exam.dto.response.ExamResponseDto;
import com.auspify_tech.online_exam_system.exam.entity.ExamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExamMapper {

    ExamResponseDto mapEntityToResponse(ExamEntity examEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ExamEntity mapCreateRequestToEntity(CreateExamRequestDto createExamRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void mapUpdateRequestToEntity(UpdateExamRequestDto updateExamRequestDto, @MappingTarget ExamEntity examEntity);

}
