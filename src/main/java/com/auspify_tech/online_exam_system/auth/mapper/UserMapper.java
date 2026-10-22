package com.auspify_tech.online_exam_system.auth.mapper;

import com.auspify_tech.online_exam_system.auth.dto.request.CreateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.UserResponseDto;
import com.auspify_tech.online_exam_system.auth.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDto mapEntityToResponseDto(UserEntity userEntity);

    UserResponseDto mapRequestToEntity(CreateUserRequestDto request);

}
