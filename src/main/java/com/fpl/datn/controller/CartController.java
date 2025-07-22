package com.fpl.datn.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.request.AddCartRequest;
import com.fpl.datn.dto.request.ChangeCartItemRequest;
import com.fpl.datn.dto.response.CartItemResponse;
import com.fpl.datn.dto.response.CartResponse;
import com.fpl.datn.service.CartService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping
    ApiResponse<CartResponse> getOrCreateCart(HttpSession session) {

        return ApiResponse.<CartResponse>builder()
                .result(cartService.getOrCreateCart(session))
                .build();
    }

    @PostMapping("/add")
    ApiResponse<CartResponse> addtoCart(@RequestBody AddCartRequest request) {
        cartService.addToCart(request);
        return ApiResponse.<CartResponse>builder()
                .message("đã thêm sản phâm vào giỏ hàng")
                .build();
    }

    @PutMapping("/update")
    ApiResponse<CartItemResponse> updateCartItemQuantity(
            HttpSession session, @RequestBody ChangeCartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .result(cartService.changeCartItemQuantity(session, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CartItemResponse>> getCart(HttpSession session) {
        return ApiResponse.<List<CartItemResponse>>builder()
                .result(cartService.getCart(session))
                .build();
    }

    @DeleteMapping("/{variantId}")
    ApiResponse<Void> getCart(HttpSession session, @PathVariable Integer variantId) {
        cartService.delete(session, variantId);
        return ApiResponse.<Void>builder().message("Delete Success!").build();
    }
}
