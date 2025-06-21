package com.fpl.datn.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.service.VnpayService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    VnpayService vnpayService;

    @GetMapping("/vnpay-return")
    public ApiResponse<String> handleVnpayReturn(@RequestParam Map<String, String> params) {
        return ApiResponse.<String>builder()
                .result(vnpayService.handleVnpayReturn(params))
                .build();
    }
}
