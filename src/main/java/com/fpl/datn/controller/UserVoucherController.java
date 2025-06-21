package com.fpl.datn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.UserVoucherResponse;
import com.fpl.datn.service.UserVoucherService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/Zvoucher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVoucherController {
    UserVoucherService userVoucherService;

    @GetMapping
    ApiResponse<PageResponse<UserVoucherResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "false") boolean sort) {
        return ApiResponse.<PageResponse<UserVoucherResponse>>builder()
                .result(userVoucherService.getAll(page, size, sort))
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<PageResponse<UserVoucherResponse>> Get(
            @PathVariable int userId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "false") boolean sort) {
        return ApiResponse.<PageResponse<UserVoucherResponse>>builder()
                .result(userVoucherService.getUserVouchers(userId, page, size, sort))
                .build();
    }

    @PostMapping("/{userId}")
    ApiResponse<UserVoucherResponse> create(@PathVariable int userId, @RequestParam String code) {
        return ApiResponse.<UserVoucherResponse>builder()
                .result(userVoucherService.create(userId, code))
                .build();
    }
}
