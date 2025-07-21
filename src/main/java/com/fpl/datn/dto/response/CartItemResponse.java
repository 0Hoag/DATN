package com.fpl.datn.dto.response;

import java.math.BigDecimal;

import com.fpl.datn.dto.response.Product.ProductVariantResponse;

public class CartItemResponse {
    Integer id;
    Integer quantity;
    BigDecimal price;
    ProductVariantResponse productVariant;
}
