package com.fpl.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChartPointIntResponse {
    String date; // YYYY-MM
    String name;
    long value;
}
