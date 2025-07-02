package com.fpl.datn.controller;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.response.ChartPointIntResponse;
import com.fpl.datn.dto.response.ChartPointResponse;
import com.fpl.datn.dto.response.TopProductResponse;
import com.fpl.datn.service.DashboardService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/total-users")
    public ApiResponse<Long> getTotalUsers() {
        return ApiResponse.<Long>builder()
                .code(1000)
                .result(dashboardService.getTotalUsers())
                .build();
    }

    @GetMapping("/total-orders")
    public ApiResponse<Long> getTotalOrders() {
        return ApiResponse.<Long>builder()
                .code(1000)
                .result(dashboardService.getTotalOrders())
                .build();
    }

    @GetMapping("/total-revenue")
    public ApiResponse<BigDecimal> getTotalRevenue() {
        return ApiResponse.<BigDecimal>builder()
                .code(1000)
                .result(dashboardService.getTotalRevenue())
                .build();
    }

    @GetMapping("/total-products-sold")
    public ApiResponse<Long> getTotalProductsSold(@RequestParam("month") int month, @RequestParam("year") int year) {
        return ApiResponse.<Long>builder()
                .code(1000)
                .result(dashboardService.getTotalProductsSold(month, year))
                .build();
    }

    @GetMapping("/top-products")
    public ApiResponse<List<TopProductResponse>> getTopProducts(@RequestParam("month") int month, @RequestParam("year") int year) {
        return ApiResponse.<List<TopProductResponse>>builder()
                .code(1000)
                .result(dashboardService.getTopProducts(month, year))
                .build();
    }

    @GetMapping("/chart/revenue")
    public ApiResponse<List<ChartPointResponse>> getRevenueChart(@RequestParam("month") int month, @RequestParam("year") int year) {
        return ApiResponse.<List<ChartPointResponse>>builder()
                .code(1000)
                .result(dashboardService.getRevenueChart(month, year))
                .build();
    }

    @GetMapping("/chart/orders")
    public ApiResponse<List<ChartPointIntResponse>> getOrderChart(@RequestParam("month") int month, @RequestParam("year") int year) {
        return ApiResponse.<List<ChartPointIntResponse>>builder()
                .code(1000)
                .result(dashboardService.getOrderChart(month, year))
                .build();
    }

    @GetMapping("/chart/products")
    public ApiResponse<List<ChartPointIntResponse>> getProductChart(@RequestParam("month") int month, @RequestParam("year") int year) {
        return ApiResponse.<List<ChartPointIntResponse>>builder()
                .code(1000)
                .result(dashboardService.getProductChart(month, year))
                .build();
    }
}
