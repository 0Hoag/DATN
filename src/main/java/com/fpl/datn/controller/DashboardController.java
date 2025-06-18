package com.fpl.datn.controller;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.response.*;
import com.fpl.datn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        return ApiResponse.<DashboardSummaryResponse>builder()
                .code(1000)
                .result(dashboardService.getSummary())
                .build();
    }

    @GetMapping("/top-products")
    public ApiResponse<List<TopProductResponse>> getTopProducts() {
        return ApiResponse.<List<TopProductResponse>>builder()
                .code(1000)
                .result(dashboardService.getTopProducts())
                .build();
    }

    @GetMapping("/revenue-chart")
    public ApiResponse<List<RevenueChartResponse>> getChart() {
        return ApiResponse.<List<RevenueChartResponse>>builder()
                .code(1000)
                .result(dashboardService.getRevenueChart())
                .build();
    }
}
