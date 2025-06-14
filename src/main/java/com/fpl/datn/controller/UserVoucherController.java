package com.fpl.datn.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.UserVoucherRequest;
import com.fpl.datn.dto.response.UserVoucherResponse;
import com.fpl.datn.service.UserVoucherService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/user-voucher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVoucherController {
    UserVoucherService userVoucherService;

    @GetMapping
    ApiResponse<PageResponse<UserVoucherResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean desc) {
        return ApiResponse.<PageResponse<UserVoucherResponse>>builder()
                .result(userVoucherService.getAll(page, size, desc))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<UserVoucherResponse> getUserVoucher(@PathVariable int id) {
        return ApiResponse.<UserVoucherResponse>builder()
                .result(userVoucherService.getUserVoucher(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<UserVoucherResponse>> getByUserId(
            @PathVariable int userId,
            @RequestParam(required = false, defaultValue = "true") boolean desc) {
        return ApiResponse.<List<UserVoucherResponse>>builder()
                .result(userVoucherService.getUserVouchersByUserId(userId, desc))
                .build();
    }

    @GetMapping("/voucher/{voucherId}")
    ApiResponse<List<UserVoucherResponse>> getByVoucherId(
            @PathVariable int voucherId,
            @RequestParam(required = false, defaultValue = "true") boolean desc) {
        return ApiResponse.<List<UserVoucherResponse>>builder()
                .result(userVoucherService.getUserVouchersByVoucherId(voucherId, desc))
                .build();
    }

    // CHỨC NĂNG 1: ADMIN PHÁT VOUCHER CHO NHIỀU USER
    @PostMapping("/bulk-assign")
    ApiResponse<List<UserVoucherResponse>> bulkAssignVoucherToUsers(@RequestBody UserVoucherRequest request) {
        return ApiResponse.<List<UserVoucherResponse>>builder()
                .result(userVoucherService.bulkAssignVoucherToUsers(request))
                .message("Phát voucher thành công!")
                .build();
    }

    // CHỨC NĂNG 2: USER TỰ NHẬN VOUCHER
    @PostMapping("/claim")
    ApiResponse<UserVoucherResponse> claimVoucher(@RequestBody UserVoucherRequest request) {
        return ApiResponse.<UserVoucherResponse>builder()
                .result(userVoucherService.claimVoucher(request))
                .message("Nhận voucher thành công!")
                .build();
    }

    @PostMapping
    ApiResponse<UserVoucherResponse> assignVoucherToUser(@RequestBody UserVoucherRequest request) {
        return ApiResponse.<UserVoucherResponse>builder()
                .result(userVoucherService.assignVoucherToUser(request))
                .build();
    }

    @PostMapping("/assign-to-all")
    ApiResponse<List<UserVoucherResponse>> assignVoucherToAllUsers(@RequestBody UserVoucherRequest request) {
        return ApiResponse.<List<UserVoucherResponse>>builder()
                .result(userVoucherService.assignVoucherToAllUsers(request))
                .message("Phát voucher cho tất cả user thành công!")
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteUserVoucher(@PathVariable int id) {
        userVoucherService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Delete Success!")
                .build();
    }

    @GetMapping("/check")
    ApiResponse<Boolean> checkUserHasVoucher(
            @RequestParam int userId,
            @RequestParam int voucherId) {
        return ApiResponse.<Boolean>builder()
                .result(userVoucherService.checkUserHasVoucher(userId, voucherId))
                .build();
    }
}