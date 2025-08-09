package com.fpl.datn.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.response.ChartPointIntResponse;
import com.fpl.datn.dto.response.ChartPointResponse;
import com.fpl.datn.dto.response.TopProductResponse;
import com.fpl.datn.repository.DashboardRepository;
import com.fpl.datn.repository.OrderDetailRepository;
import com.fpl.datn.repository.TransactionLogRepository;

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
    TransactionLogRepository transactionLogRepository;
    OrderDetailRepository orderDetailRepository;

    public long getTotalUsers() {
        return dashboardRepository.countTotalUsers();
    }

    public long getTotalOrders() {
        return dashboardRepository.countTotalOrders();
    }

    public long getTotalProductsSold() {
        return dashboardRepository.totalProductsSold();
    }

    public BigDecimal getTotalRevenue() {
        return transactionLogRepository.getTotalRevenue();
    }

    public List<TopProductResponse> getTopProducts(int year) {
        return dashboardRepository.findTop10ProductsSoldByYearDto(year);
    }

    public List<ChartPointResponse> getMonthlyRevenueThisYear() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();

        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999_999_999);

        List<ChartPointResponse> rawData = transactionLogRepository.getMonthlyRevenueOfYear(startDate, endDate);

        // Đưa về dạng đủ 12 tháng, kể cả tháng không có dữ liệu
        Map<Integer, BigDecimal> revenueMap = new HashMap<>();
        for (ChartPointResponse cp : rawData) {
            revenueMap.put(cp.getMonth(), cp.getValue());
        }

        List<ChartPointResponse> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            result.add(new ChartPointResponse(month, revenueMap.getOrDefault(month, BigDecimal.ZERO)));
        }

        return result;
    }

    public List<ChartPointIntResponse> getMonthlyProductSold() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();

        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999_999_999);

        List<ChartPointIntResponse> rawData = orderDetailRepository.getMonthlyProductSold(startDate, endDate);

        // Đưa về dạng đủ 12 tháng, kể cả tháng không có dữ liệu
        Map<Integer, Long> quantityMap = new HashMap<>();
        for (ChartPointIntResponse cp : rawData) {
            quantityMap.put(cp.getMonth(), cp.getValue());
        }

        List<ChartPointIntResponse> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            result.add(new ChartPointIntResponse(month, quantityMap.getOrDefault(month, 0L)));
        }

        return result;
    }

    public List<ChartPointIntResponse> getMonthlyOrderCount() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();

        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999_999_999);

        List<ChartPointIntResponse> rawData = dashboardRepository.getMonthlyOrderCount(startDate, endDate);

        // Đưa về dạng đủ 12 tháng, kể cả tháng không có dữ liệu
        Map<Integer, Long> quantityMap = new HashMap<>();
        for (ChartPointIntResponse cp : rawData) {
            quantityMap.put(cp.getMonth(), cp.getValue());
        }

        List<ChartPointIntResponse> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            result.add(new ChartPointIntResponse(month, quantityMap.getOrDefault(month, 0L)));
        }

        return result;
    }
}
