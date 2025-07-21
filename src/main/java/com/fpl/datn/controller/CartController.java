package com.fpl.datn.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
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
        String sessionId = session.getId();
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getOrCreateCart(sessionId))
                .build();
    }
}
