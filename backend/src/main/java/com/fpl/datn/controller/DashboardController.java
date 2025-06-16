package com.fpl.datn.controller;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.response.DashboardResponse;
import com.fpl.datn.service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<DashboardResponse> getStats() {
        return ApiResponse.<DashboardResponse>builder()
                .code(1000)
                .result(dashboardService.getDashboardStats())
                .build();
    }
}
