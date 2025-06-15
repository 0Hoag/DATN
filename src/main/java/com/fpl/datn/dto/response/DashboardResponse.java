package com.fpl.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardResponse {
    Long totalUsers;
    Long totalOrders;
    BigDecimal totalRevenue;
    Long totalProductsSold;
    Long totalProducts;
    List<TopProductResponse> topProducts;
    List<ChartDataPoint> revenueChart;
    List<ChartDataPoint> orderChart;
    List<ChartDataPoint> productChart;
}
