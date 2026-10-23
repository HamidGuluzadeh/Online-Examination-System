package com.auspify_tech.online_exam_system.auth.service.implement;

import com.auspify_tech.online_exam_system.auth.dto.request.ChangePasswordRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.CreateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.ResetPasswordRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.request.UpdateUserRequestDto;
import com.auspify_tech.online_exam_system.auth.dto.response.UserResponseDto;
import com.auspify_tech.online_exam_system.auth.entity.UserEntity;
import com.auspify_tech.online_exam_system.auth.mapper.UserMapper;
import com.auspify_tech.online_exam_system.auth.model.UserStatus;
import com.auspify_tech.online_exam_system.auth.repository.UserRepository;
import com.auspify_tech.online_exam_system.auth.service.UserService;
import com.auspify_tech.online_exam_system.common.exception.BadRequestException;
import com.auspify_tech.online_exam_system.common.exception.ConflictException;
import com.auspify_tech.online_exam_system.common.exception.ResourceAlreadyExistsException;
import com.auspify_tech.online_exam_system.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserEntity> userEntities = userRepository.findAll(pageable);

        return userEntities.map(userMapper::mapEntityToResponseDto);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return userMapper.mapEntityToResponseDto(userEntity);
    }

    @Override
    public UserResponseDto getUserById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return userMapper.mapEntityToResponseDto(userEntity);
    }

    @Override
    public UserResponseDto getMyProfile() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return userMapper.mapEntityToResponseDto(userEntity);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        if (userRepository.existsByUsername(createUserRequestDto.username())) {
            throw new ResourceAlreadyExistsException("User with username " +
                    createUserRequestDto.username() + " already exists!");
        }

        if (userRepository.existsByEmail(createUserRequestDto.email())) {
            throw new ResourceAlreadyExistsException("User with email " +
                    createUserRequestDto.email() + " already exists!");
        }

        if (userRepository.existsByPhoneNumber(createUserRequestDto.phoneNumber())) {
            throw new ResourceAlreadyExistsException("User with phone number " +
                    createUserRequestDto.phoneNumber() + " already exists!");
        }

        UserEntity userEntity = userMapper.mapRequestToEntity(createUserRequestDto);
        userEntity.setPassword(passwordEncoder.encode(createUserRequestDto.password()));
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCreatedAt(Instant.now());

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return userMapper.mapEntityToResponseDto(savedUserEntity);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(String id, UpdateUserRequestDto updateUserRequestDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (userRepository.existsByUsernameAndIdNot(updateUserRequestDto.username(), id)) {
            throw new ResourceAlreadyExistsException("User with username " +
                    updateUserRequestDto.username() + " already exists!");
        }

        if (userRepository.existsByEmailAndIdNot(updateUserRequestDto.email(), id)) {
            throw new ResourceAlreadyExistsException("User with email " +
                    updateUserRequestDto.email() + " already exists!");
        }

        if (userRepository.existsByPhoneNumberAndIdNot(updateUserRequestDto.phoneNumber(), id)) {
            throw new ResourceAlreadyExistsException("User with phone number " +
                    updateUserRequestDto.phoneNumber() + " already exists!");
        }

        userMapper.updateEntityFromRequest(updateUserRequestDto, userEntity);
        userEntity.setUpdatedAt(Instant.now());

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return userMapper.mapEntityToResponseDto(savedUserEntity);
    }

    @Override
    @Transactional
    public void changeStatus(String id, UserStatus userStatus) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        userEntity.setStatus(userStatus);
        userEntity.setUpdatedAt(Instant.now());
    }

    @Override
    @Transactional
    public void resetPassword(String id, ResetPasswordRequestDto resetPasswordRequestDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (passwordEncoder.matches(resetPasswordRequestDto.newPassword(), userEntity.getPassword())) {
            throw new ConflictException("New password is the same with old password!");
        }

        if (!resetPasswordRequestDto.newPassword().equals(resetPasswordRequestDto.confirmPassword())) {
            throw new BadRequestException("Passwords don't match each other!");
        }

        userEntity.setPassword(passwordEncoder.encode(resetPasswordRequestDto.confirmPassword()));
        userEntity.setUpdatedAt(Instant.now());
    }

    @Override
    @Transactional
    public void changeMyPassword(ChangePasswordRequestDto changePasswordRequestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (!passwordEncoder.matches(changePasswordRequestDto.oldPassword(), userEntity.getPassword())) {
            throw new BadRequestException("Old password is incorrect!");
        }

        if (changePasswordRequestDto.newPassword().equals(changePasswordRequestDto.oldPassword())) {
            throw new ConflictException("New password is the same with old password!");
        }

        if (!changePasswordRequestDto.newPassword().equals(changePasswordRequestDto.confirmPassword())) {
            throw new BadRequestException("Passwords don't match each other!");
        }

        userEntity.setPassword(passwordEncoder.encode(changePasswordRequestDto.confirmPassword()));
        userEntity.setUpdatedAt(Instant.now());
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found!");
        }

        userRepository.deleteById(id);
    }
}
