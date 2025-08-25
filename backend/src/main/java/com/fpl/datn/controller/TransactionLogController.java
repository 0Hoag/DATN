package com.fpl.datn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.TransactionlogResponse;
import com.fpl.datn.service.TransactionLogService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionLogController {
    TransactionLogService transactionLogService;

    @GetMapping
    ApiResponse<PageResponse<TransactionlogResponse>> GetAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean sort) {
        return ApiResponse.<PageResponse<TransactionlogResponse>>builder()
                .result(transactionLogService.Get(page, size, sort))
                .build();
    }
}