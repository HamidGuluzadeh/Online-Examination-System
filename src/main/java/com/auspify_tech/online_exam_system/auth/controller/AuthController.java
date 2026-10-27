package com.auspify_tech.online_exam_system.auth.controller;

import com.auspify_tech.online_exam_system.auth.dto.request.LoginRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.RefreshRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.AuthResponseDto;
import com.auspify_tech.online_exam_system.auth.service.AuthService;
import com.auspify_tech.online_exam_system.common.dto.SuccessDto;
import com.auspify_tech.online_exam_system.common.model.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Authentication Management")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @Operation(summary = "Log in")
    @PostMapping("/login")
    public ResponseEntity<SuccessDto<AuthResponseDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        AuthResponseDto authResponseDto = authService.login(loginRequestDto);
        SuccessDto<AuthResponseDto> authResponseSuccessDto = new SuccessDto<>(SuccessStatus.SUCCESS, authResponseDto);
        return new ResponseEntity<>(authResponseSuccessDto, HttpStatus.OK);
    }

    @Operation(summary = "Get a new refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<SuccessDto<AuthResponseDto>> refresh(@RequestBody @Valid RefreshRequestDto refreshRequestDto) {
        AuthResponseDto authResponseDto = authService.refresh(refreshRequestDto);
        SuccessDto<AuthResponseDto> authResponseSuccessDto = new SuccessDto<>(SuccessStatus.SUCCESS, authResponseDto);
        return new ResponseEntity<>(authResponseSuccessDto, HttpStatus.OK);
    }

    @Operation(summary = "Log out")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
