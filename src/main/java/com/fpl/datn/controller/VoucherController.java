package com.fpl.datn.controller;

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
    ApiResponse<PageResponse<VoucherResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean desc) {
        return ApiResponse.<PageResponse<VoucherResponse>>builder()
                .result(voucherService.getAll(page, size, desc))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<VoucherResponse> getVoucher(@PathVariable int id) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getVoucher(id))
                .build();
    }

    @PostMapping
    ApiResponse<VoucherResponse> createVoucher(@RequestBody VoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<VoucherResponse> updateVoucher(@PathVariable int id, @RequestBody UpdateVoucherRequest request) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteVoucher(@PathVariable int id) {
        voucherService.delete(id);
        return ApiResponse.<Void>builder().message("Delete Success!").build();
    }
}
