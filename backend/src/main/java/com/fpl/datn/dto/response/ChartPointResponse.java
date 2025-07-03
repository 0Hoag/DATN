package com.fpl.datn.dto.response;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChartPointResponse {
    String name;
    BigDecimal value;
    Integer month;
    Integer year;
}
