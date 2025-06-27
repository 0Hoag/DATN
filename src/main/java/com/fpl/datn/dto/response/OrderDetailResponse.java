package com.fpl.datn.dto.response;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Integer id;
    BigDecimal price;
    Integer quantity;
    // tến sản phẩm
    String name;
    // tên biến thể sản phẩm
    String variantName;
}
