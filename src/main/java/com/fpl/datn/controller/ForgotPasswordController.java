package com.fpl.datn.controller;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.*;
import com.fpl.datn.dto.response.UserResponse;
import com.fpl.datn.service.EmailService;
import com.fpl.datn.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgotPasswordController {
    EmailService emailService;

    @PostMapping("/verifyMail/{email}")
    ApiResponse<String> verifyEmail(@PathVariable String email) {
        return ApiResponse.<String>builder()
                .code(1000)
                .result(emailService.verifyEmail(email))
                .build();
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    ApiResponse<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        return ApiResponse.<String>builder()
                .code(1000)
                .result(emailService.verifyOtp(otp, email))
                .build();
    }

    // when verifyOtp success change page and call this API
    @PostMapping("/changePassword/{email}")
    ApiResponse<String> changePassword(@PathVariable String email, @RequestBody ChangePasswordVerifySuccessRequest request) {
        return ApiResponse.<String>builder()
                .code(1000)
                .result(emailService.changePassword(email, request))
                .build();
    }
}
