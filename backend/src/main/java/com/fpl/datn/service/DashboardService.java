package com.fpl.datn.service;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.response.*;
import com.fpl.datn.repository.DashboardRepository;
import java.math.BigDecimal;
import lombok.AccessLevel;
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

    public long getTotalProductsSold(int month, int year) {
        return dashboardRepository.countTotalProductsSold(month, year);
    }

    public List<TopProductResponse> getTopProducts(int month, int year) {
        return dashboardRepository.findTop10ProductsSoldByMonthYearDto(month, year);
    }

    public List<ChartPointResponse> getRevenueChart(int month, int year) {
        return dashboardRepository.getRevenueChart(month, year);
    }

    public List<ChartPointIntResponse> getOrderChart(int month, int year) {
        return dashboardRepository.getOrderChart(month, year);
    }

    public List<ChartPointIntResponse> getProductChart(int month, int year) {
        return dashboardRepository.getProductChart(month, year);
    }
}
