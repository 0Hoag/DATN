package com.fpl.datn.dto.response;

import java.math.BigDecimal;

import com.fpl.datn.dto.response.Product.ProductVariantResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    Integer id;
    Integer quantity;
    BigDecimal price;
    ProductVariantResponse productVariant;
}
