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
        BigDecimal totalRevenue = dashboardService.getTotalRevenue();
        System.out.println("Tổng doanh thu: " + totalRevenue);
        return ApiResponse.<BigDecimal>builder().code(1000).result(totalRevenue).build();
    }

    @GetMapping("/total-products-sold")
    public ApiResponse<Long> getTotalProductsSold() {
        return ApiResponse.<Long>builder()
                .code(1000)
                .result(dashboardService.getTotalProductsSold())
                .build();
    }

    @GetMapping("/top-products")
    public ApiResponse<List<TopProductResponse>> getTopProducts(@RequestParam int year) {
        return ApiResponse.<List<TopProductResponse>>builder()
                .code(1000)
                .result(dashboardService.getTopProducts(year))
                .build();
    }

    @GetMapping("/monthly-revenue")
    public ApiResponse<List<ChartPointResponse>> getMonthlyRevenue() {
        return ApiResponse.<List<ChartPointResponse>>builder()
                .code(1000)
                .result(dashboardService.getMonthlyRevenueThisYear())
                .build();
    }

    @GetMapping("/monthly-product-sold")
    public ApiResponse<List<ChartPointIntResponse>> getMonthlyProductSold() {
        return ApiResponse.<List<ChartPointIntResponse>>builder()
                .code(1000)
                .result(dashboardService.getMonthlyProductSold())
                .build();
    }

    @GetMapping("/monthly-order")
    public ApiResponse<List<ChartPointIntResponse>> getMonthlyOrder() {
        return ApiResponse.<List<ChartPointIntResponse>>builder()
                .code(1000)
                .result(dashboardService.getMonthlyOrderCount())
                .build();
    }
}
