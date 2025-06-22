package com.fpl.datn.dto.response;

import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardResponse {
    long totalCustomers;
    long totalOrders;
    BigDecimal totalRevenue;
    long totalProductsSold;
    List<TopProductResponse> topProducts;
    ChartData chartData;
}
