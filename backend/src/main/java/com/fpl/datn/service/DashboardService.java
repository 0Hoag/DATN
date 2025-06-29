package com.fpl.datn.service;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.response.*;
import com.fpl.datn.repository.DashboardRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DashboardService {

    DashboardRepository dashboardRepository;

    public DashboardResponse getShow() {
        int month = java.time.LocalDate.now().getMonthValue();
        int year = java.time.LocalDate.now().getYear();
        return DashboardResponse.builder()
                .totalCustomers(dashboardRepository.countTotalUsers())
                .totalOrders(dashboardRepository.countTotalOrders())
                .totalRevenue(dashboardRepository.sumTotalRevenue())
                .totalProductsSold(dashboardRepository.countTotalProductsSold())
                .topProducts(dashboardRepository.findTop10ProductsSoldByMonthYearDto(month, year))
                .chartData(ChartData.builder()
                        .revenueChart(dashboardRepository.getRevenueChart())
                        .orderChart(dashboardRepository.getOrderChart())
                        .productChart(dashboardRepository.getProductChart())
                        .build())
                .build();
    }
}
