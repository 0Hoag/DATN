package com.fpl.datn.service;

import com.fpl.datn.dto.response.DashboardResponse;
import com.fpl.datn.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DashboardService {

    DashboardRepository dashboardRepository;

    @PreAuthorize("hasRole('ADMIN')")
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
