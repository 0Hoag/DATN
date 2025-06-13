package com.fpl.datn.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.models.TransactionLog;
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
    ApiResponse<List<TransactionLog>> GetAll() {
        return ApiResponse.<List<TransactionLog>>builder()
                .result(transactionLogService.Get())
                .build();
    }
}
