package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpl.datn.constant.PredefinedRole;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.RegisterRequest;
import com.fpl.datn.dto.request.UpdateProfileRequest;
import com.fpl.datn.dto.request.UpdateUserRequest;
import com.fpl.datn.dto.request.UserRequest;
import com.fpl.datn.dto.response.UserResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.UserMapper;
import com.fpl.datn.models.Role;
import com.fpl.datn.models.User;
import com.fpl.datn.repository.RoleRepository;
import com.fpl.datn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepositories;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public UserResponse Create(UserRequest request) {
        try {
            HashSet<Role> roles = roleRepository.findAllByNameIn(request.getRoles());

            if (!userRepositories.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            if (!userRepositories.existsByPhone(request.getPhone())) {
                throw new AppException(ErrorCode.PHONE_EXISTED);
            }

            User user = userMapper.toUser(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setIsEnable(true);
            user.setRoles(roles);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            userRepositories.save(user);
            return userMapper.toUserResponse(user);
        } catch (AppException e) {
            throw new AppException(ErrorCode.ERROR_CREATE_USER);
        }
    }

    public UserResponse Update(int id, UpdateUserRequest request) {
        var user = userRepositories.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepositories.save(user));
    }

    public UserResponse Detail(int id) {
        try {
            User user = userRepositories.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            return userMapper.toUserResponse(user);
        } catch (AppException e) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    public void Delete(int id) {
        if (!userRepositories.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        try {
            userRepositories.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.UNCATEGORIZE_EXCEPTION);
        }
    }

    public List<UserResponse> List() {
        return userRepositories.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<UserResponse> Get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = userRepositories.findAll(pageable);

        var data =
                pageData.getContent().stream().map(userMapper::toUserResponse).collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public UserResponse Register(RegisterRequest request) {
        try {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.builder()
                    .name(PredefinedRole.ROLE_CUSTOMER)
                    .description("Customer role")
                    .build());

            if (userRepositories.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            if (userRepositories.existsByPhone(request.getPhone())) {
                throw new AppException(ErrorCode.PHONE_EXISTED);
            }

            User user = userMapper.toUserRegister(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setIsEnable(true);
            user.setRoles(roles);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            userRepositories.save(user);
            return userMapper.toUserResponse(user);
        } catch (AppException e) {
            throw new AppException(ErrorCode.ERROR_CREATE_USER);
        }
    }

    public UserResponse UpdateProfile(int id, UpdateProfileRequest request) {
        try {
            User user = userRepositories.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            userMapper.updateProfile(user, request);
            return userMapper.toUserResponse(userRepositories.save(user));
        } catch (AppException e) {
            throw new AppException(ErrorCode.ERROR_UPDATE_USER);
        }
    }
}
