package com.fpl.datn.dto.response;

import java.math.BigDecimal;

import com.fpl.datn.dto.response.Product.ProductVariantResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Integer id;
    Integer quantity;
    BigDecimal price;
    ProductVariantResponse productVariant;
}
