package com.fpl.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueChartResponse {
    String date;
    long orderCount;
    long productsSold;
    BigDecimal totalRevenue;
}
