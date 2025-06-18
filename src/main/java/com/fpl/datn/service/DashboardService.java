package com.fpl.datn.service;

import com.fpl.datn.dto.response.*;
import com.fpl.datn.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DashboardService {

    DashboardRepository dashboardRepository;

    public DashboardSummaryResponse getSummary() {
        return DashboardSummaryResponse.builder()
                .totalCustomers(dashboardRepository.countTotalUsers())
                .totalOrders(dashboardRepository.countTotalOrders())
                .totalRevenue(dashboardRepository.sumTotalRevenue())
                .totalProductsSold(dashboardRepository.countTotalProductsSold())
                .build();
    }

    public List<TopProductResponse> getTopProducts() {
        return dashboardRepository.findTop10Products();
    }

    public List<RevenueChartResponse> getRevenueChart() {
        return dashboardRepository.getDailyChart();
    }
}
