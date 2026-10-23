package com.auspify_tech.online_exam_system.auth.mapper;

import com.auspify_tech.online_exam_system.auth.dto.request.CreateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.UpdateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.UserResponseDto;
import com.auspify_tech.online_exam_system.auth.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDto mapEntityToResponseDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity mapRequestToEntity(CreateUserRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateUserRequestDto request, @MappingTarget UserEntity userEntity);

}
