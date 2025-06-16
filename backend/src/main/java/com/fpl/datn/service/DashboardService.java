package com.fpl.datn.service;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.response.DashboardResponse;
import com.fpl.datn.repository.DashboardRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DashboardService {

    DashboardRepository dashboardRepository;

    public DashboardResponse getDashboardStats() {
        return DashboardResponse.builder()
                .totalUsers(dashboardRepository.countTotalUsers())
                .totalOrders(dashboardRepository.countTotalOrders())
                .totalRevenue(dashboardRepository.sumTotalRevenue())
                .totalProducts(dashboardRepository.countTotalProducts())
                .totalProductsSold(dashboardRepository.countTotalProductsSold())
                .topProducts(dashboardRepository.top10SellingProducts())
                .revenueChart(dashboardRepository.revenueChart())
                .orderChart(dashboardRepository.orderChart())
                .productChart(dashboardRepository.productChart())
                .build();
    }
}
