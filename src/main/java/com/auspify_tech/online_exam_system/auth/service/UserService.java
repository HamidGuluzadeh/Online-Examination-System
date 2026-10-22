package com.auspify_tech.online_exam_system.auth.service;

import com.auspify_tech.online_exam_system.auth.dto.request.ChangePasswordRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.CreateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.ResetPasswordRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.UpdateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.UserResponseDto;
import com.auspify_tech.online_exam_system.auth.model.UserStatus;
import org.springframework.data.domain.Page;

public interface UserService {

    Page<UserResponseDto> getAllUsers(int page, int size);

    UserResponseDto getUserByUsername(String username);

    UserResponseDto getUserById(String id);

    UserResponseDto getMyProfile();

    UserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    UserResponseDto updateUser(String id, UpdateUserRequestDto updateUserRequestDto);

    void changeStatus(String employeeCode, UserStatus userStatus);

    void resetPassword(String employeeCode, ResetPasswordRequestDto resetPasswordRequestDto);

    void changeMyPassword(ChangePasswordRequestDto changePasswordRequestDto);

    void deleteUser(String id);

}
