package com.fpl.datn.service;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.response.*;
import com.fpl.datn.repository.DashboardRepository;
import java.math.BigDecimal;
import lombok.AccessLevel;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DashboardService {

    DashboardRepository dashboardRepository;

    public long getTotalUsers() {
        return dashboardRepository.countTotalUsers();
    }

    public long getTotalOrders() {
        return dashboardRepository.countTotalOrders();
    }

    public BigDecimal getTotalRevenue() {
        return dashboardRepository.sumTotalRevenue();
    }

    public long getTotalProductsSold(int year) {
        return dashboardRepository.countTotalProductsSold(year);
    }

    public List<TopProductResponse> getTopProducts(int year) {
        return dashboardRepository.findTop10ProductsSoldByYearDto(year);
    }

    public List<ChartPointResponse> getRevenueChart(int year) {
        return dashboardRepository.getRevenueChart(year);
    }

    public List<ChartPointIntResponse> getOrderChart(int year) {
        return dashboardRepository.getOrderChart(year);
    }

    public List<ChartPointIntResponse> getProductChart(int year) {
        return dashboardRepository.getProductChart(year);
    }

    public DashboardResponse getDashboardData(int year) {
        return DashboardResponse.builder()
                .totalCustomers(getTotalUsers())
                .totalOrders(getTotalOrders())
                .totalRevenue(getTotalRevenue())
                .totalProductsSold(getTotalProductsSold(year))
                .topProducts(getTopProducts(year))
                .chartData(ChartData.builder()
                        .revenueChart(getRevenueChart(year))
                        .orderChart(getOrderChart(year))
                        .productChart(getProductChart(year))
                        .build())
                .build();
    }
}
