package com.fpl.datn.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChartData {
    List<ChartPointResponse> revenueChart;
    List<ChartPointIntResponse> orderChart;
    List<ChartPointIntResponse> productChart;
}
