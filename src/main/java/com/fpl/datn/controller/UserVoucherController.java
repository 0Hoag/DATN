package com.fpl.datn.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.UserVoucherClaimRequest;
import com.fpl.datn.dto.request.ZUserVoucherAssignAllRequest;
import com.fpl.datn.dto.response.VoucherResponse;
import com.fpl.datn.dto.response.ZUserVoucherResponse;
import com.fpl.datn.service.UserVoucherService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-vouchers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVoucherController {
    UserVoucherService zUserVoucherService;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_USER_VOUCHER') or hasRole('ADMIN')")
    public ApiResponse<PageResponse<ZUserVoucherResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ZUserVoucherResponse>>builder()
                .result(zUserVoucherService.getAll(page, size)) // Gọi phương thức getAll với 2 tham số
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_USER_VOUCHER') or hasRole('ADMIN') or hasRole('USER')")
    public ApiResponse<ZUserVoucherResponse> getZUserVoucher(@PathVariable int id) {
        return ApiResponse.<ZUserVoucherResponse>builder()
                .result(zUserVoucherService.getZUserVoucher(id))
                .build();
    }

    @PostMapping("/assign-to-all")
    @PreAuthorize("hasAuthority('ASSIGN_VOUCHER') or hasRole('ADMIN')")
    public ApiResponse<Void> assignVoucherToAllUsers(@Valid @RequestBody ZUserVoucherAssignAllRequest request) {
        zUserVoucherService.assignVoucherToAllUsers(request);
        return ApiResponse.<Void>builder()
                .message("Gán voucher cho tất cả người dùng thành công!")
                .build();
    }

    @PostMapping("/claim")
    @PreAuthorize("hasAuthority('CLAIM_VOUCHER') or hasRole('USER')")
    public ApiResponse<ZUserVoucherResponse> claimVoucher(@Valid @RequestBody UserVoucherClaimRequest request) {
        return ApiResponse.<ZUserVoucherResponse>builder()
                .result(zUserVoucherService.claimVoucher(request))
                .message("Yêu cầu nhận voucher thành công!")
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER_VOUCHER') or hasRole('ADMIN')")
    public ApiResponse<Void> deleteZUserVoucher(@PathVariable int id) {
        zUserVoucherService.deleteZUserVoucher(id);
        return ApiResponse.<Void>builder()
                .message("Xóa voucher người dùng thành công!")
                .build();
    }

    @GetMapping("/available-count")
    @PreAuthorize("hasAuthority('VIEW_USER_VOUCHER') or hasRole('USER')")
    public ApiResponse<Long> getAvailableVoucherCount() {
        return ApiResponse.<Long>builder()
                .result(zUserVoucherService.getAvailableVoucherCountForCurrentUser())
                .message("Lấy số lượng voucher khả dụng thành công!")
                .build();
    }

    // API để lấy danh sách voucher mà người dùng có thể sử dụng
    @GetMapping("/can-use")
    @PreAuthorize("hasAuthority('VIEW_USER_VOUCHER') or hasRole('USER')")
    public ApiResponse<PageResponse<VoucherResponse>> getVouchersUserCanUse(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<VoucherResponse>>builder()
                .result(zUserVoucherService.getVouchersUserCanUse(page, size))
                .message("Lấy danh sách voucher người dùng có thể sử dụng thành công!")
                .build();
    }
}
