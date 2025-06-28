package com.fpl.datn.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.UpdateVoucherRequest;
import com.fpl.datn.dto.request.VoucherRequest;
import com.fpl.datn.dto.response.VoucherResponse;
import com.fpl.datn.service.VoucherService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {
    VoucherService voucherService;

    @GetMapping
    public ApiResponse<PageResponse<VoucherResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean desc) {
        return ApiResponse.<PageResponse<VoucherResponse>>builder()
                .result(voucherService.getAll(page, size, desc))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VoucherResponse> getVoucher(@PathVariable int id) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getVoucher(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<VoucherResponse> create(@RequestBody VoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<VoucherResponse> update(@PathVariable int id, @RequestBody UpdateVoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<Void> delete(@PathVariable int id) {
        voucherService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Delete Success!")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<VoucherResponse>> searchVoucher(
            @RequestParam String keyword,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean desc) {
        return ApiResponse.<PageResponse<VoucherResponse>>builder()
                .result(voucherService.search(keyword, page, size, desc))
                .build();
    }

    // API mới để quản lý voucher hết hạn
    @GetMapping("/expired/count")
    public ApiResponse<Long> getExpiredVouchersCount() {
        return ApiResponse.<Long>builder()
                .result(voucherService.countExpiredVouchers())
                .message("Số lượng voucher hết hạn")
                .build();
    }

    @PostMapping("/update-expired")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<Void> updateExpiredVouchers() {
        voucherService.updateExpiredVouchers();
        return ApiResponse.<Void>builder()
                .message("Đã cập nhật trạng thái voucher hết hạn!")
                .build();
    }

    @GetMapping("/expiring")
    public ApiResponse<PageResponse<VoucherResponse>> getExpiringVouchers(
            @RequestParam(required = false, defaultValue = "7") int days,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<VoucherResponse>>builder()
                .result(voucherService.getExpiringVouchers(days, page, size))
                .message("Danh sách voucher sắp hết hạn trong " + days + " ngày")
                .build();
    }
}
