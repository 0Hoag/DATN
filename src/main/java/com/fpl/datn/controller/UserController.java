package com.fpl.datn.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fpl.datn.dto.request.*;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.service.EmailService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.UserResponse;
import com.fpl.datn.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/registration")
    ApiResponse<UserResponse> registration(@RequestBody @Valid RegisterRequest request)
            throws SQLException, IOException {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.Register(request))
                .build();
    }

    @PostMapping("/create")
    ApiResponse<Boolean> create(@RequestBody @Valid UserRequest request) throws SQLException, IOException {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(userService.Create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> list() {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        authenticated.getAuthorities().stream().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .result(userService.List())
                .build();
    }

    @GetMapping("/get")
    ApiResponse<PageResponse<UserResponse>> get(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .code(1000)
                .result(userService.Get(page, size))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> detail(@PathVariable("id") int id) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.Detail(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> update(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.Update(id, request))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/profile/{id}")
    ApiResponse<UserResponse> updateProfile(@PathVariable int id, @RequestBody UpdateProfileRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.UpdateProfile(id, request))
                .build();
    }

    @PutMapping("/password")
    ApiResponse<Boolean> changePassword(@RequestBody ChangePasswordRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(userService.changePassword(request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> delete(@PathVariable int id) {
        userService.Delete(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("User has been delete")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<UserResponse>> searchUser(
            @RequestParam String keyword,
            @RequestParam(value = "role") String roleName,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .code(1000)
                .result(userService.search(keyword, roleName, page, size))
                .build();
    }
}
