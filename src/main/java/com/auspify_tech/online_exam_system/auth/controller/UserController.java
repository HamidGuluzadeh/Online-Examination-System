package com.auspify_tech.online_exam_system.auth.controller;

import com.auspify_tech.online_exam_system.auth.dto.request.ChangePasswordRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.CreateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.ResetPasswordRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.UpdateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.UserResponseDto;
import com.auspify_tech.online_exam_system.auth.model.UserStatus;
import com.auspify_tech.online_exam_system.auth.service.UserService;
import com.auspify_tech.online_exam_system.common.dto.SuccessDto;
import com.auspify_tech.online_exam_system.common.model.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Controller", description = "User Management")
public class UserController {
    UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users")
    public ResponseEntity<SuccessDto<Page<UserResponseDto>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size) {
        Page<UserResponseDto> response = userService.getAllUsers(page, size);
        SuccessDto<Page<UserResponseDto>> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by username")
    public ResponseEntity<SuccessDto<UserResponseDto>> getUserByUsername(@PathVariable String username) {
        UserResponseDto response = userService.getUserByUsername(username);
        SuccessDto<UserResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<SuccessDto<UserResponseDto>> getUserById(@PathVariable String id) {
        UserResponseDto response = userService.getUserById(id);
        SuccessDto<UserResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/me")
    @Operation(summary = "Get my profile")
    public ResponseEntity<SuccessDto<UserResponseDto>> getMyProfile() {
        UserResponseDto response = userService.getMyProfile();
        SuccessDto<UserResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user")
    public ResponseEntity<SuccessDto<UserResponseDto>> createUser(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        UserResponseDto response = userService.createUser(createUserRequestDto);
        SuccessDto<UserResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user info by ID")
    public ResponseEntity<SuccessDto<UserResponseDto>> updateUser(@PathVariable String id,
                                                                  @RequestBody @Valid UpdateUserRequestDto updateUserRequestDto) {
        UserResponseDto response = userService.updateUser(id, updateUserRequestDto);
        SuccessDto<UserResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, response);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change user status by ID")
    public ResponseEntity<Void> changeStatus(@PathVariable String id, @RequestParam UserStatus userStatus) {
        userService.changeStatus(id, userStatus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reset user login password by ID")
    public ResponseEntity<Void> resetPassword(@PathVariable String id,
                                              @RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
        userService.resetPassword(id, resetPasswordRequestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/password")
    @Operation(summary = "Change my password")
    public ResponseEntity<Void> changeMyPassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto) {
        userService.changeMyPassword(changePasswordRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
