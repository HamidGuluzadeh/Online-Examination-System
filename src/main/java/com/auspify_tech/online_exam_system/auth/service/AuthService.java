package com.auspify_tech.online_exam_system.auth.service;

import com.auspify_tech.online_exam_system.auth.dto.request.CreateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.LoginRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.RefreshRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.AuthResponseDto;

public interface AuthService {

    AuthResponseDto login(LoginRequestDto loginRequestDto);

    AuthResponseDto refresh(RefreshRequestDto refreshRequestDto);

    void logout();

}
