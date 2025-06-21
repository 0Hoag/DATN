package com.fpl.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZUserVoucherResponse {
    private Integer id;
    private Integer userId;
    private Integer voucherId;
}
