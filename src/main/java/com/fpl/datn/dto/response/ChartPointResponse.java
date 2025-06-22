package com.fpl.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChartPointResponse {
    String date; // YYYY-MM
    String name;
    BigDecimal value;
}
