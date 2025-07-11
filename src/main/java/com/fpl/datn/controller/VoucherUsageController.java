package com.fpl.datn.controller;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.service.VoucherUsageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher-usage")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherUsageController {

    VoucherUsageService voucherUsageService;

    // Kiểm tra user có thể sử dụng voucher không
    @GetMapping("/can-use")
    public ApiResponse<Boolean> canUserUseVoucher(
            @RequestParam Integer userId,
            @RequestParam Integer voucherId) {
        return ApiResponse.<Boolean>builder()
                .result(voucherUsageService.canUserUseVoucher(userId, voucherId))
                .message("Kiểm tra khả năng sử dụng voucher")
                .build();
    }

    // Ghi lại việc sử dụng voucher
    @PostMapping("/mark-used")
    public ApiResponse<Void> markVoucherAsUsed(
            @RequestParam Integer userId,
            @RequestParam Integer voucherId) {
        // Cần lấy User và Voucher entity
        // voucherUsageService.recordVoucherUsage(user, voucher);
        return ApiResponse.<Void>builder()
                .message("Đánh dấu voucher đã sử dụng")
                .build();
    }

    // Kiểm tra user đã sử dụng voucher chưa
    @GetMapping("/check-used")
    public ApiResponse<Boolean> checkVoucherUsed(
            @RequestParam Integer userId,
            @RequestParam Integer voucherId) {
        return ApiResponse.<Boolean>builder()
                .result(voucherUsageService.hasUserUsedVoucher(userId, voucherId))
                .message("Kiểm tra voucher đã sử dụng")
                .build();
    }
}