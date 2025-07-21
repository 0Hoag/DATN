package com.fpl.datn.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.service.VoucherSharingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/voucher-sharing")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherSharingController {

    VoucherSharingService voucherSharingService;

    @GetMapping("/{voucherId}/social-content")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<Map<String, String>> getSocialShareContent(@PathVariable Integer voucherId) {
        return ApiResponse.<Map<String, String>>builder()
                .result(voucherSharingService.createSocialShareContent(voucherId))
                .message("Nội dung chia sẻ social media")
                .build();
    }

    @GetMapping("/{voucherId}/qr-content")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<String> getQRCodeContent(@PathVariable Integer voucherId) {
        return ApiResponse.<String>builder()
                .result(voucherSharingService.createQRCodeContent(voucherId))
                .message("Nội dung QR code")
                .build();
    }

    @GetMapping("/{voucherId}/deep-link")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<String> getDeepLink(@PathVariable Integer voucherId) {
        return ApiResponse.<String>builder()
                .result(voucherSharingService.createDeepLink(voucherId))
                .message("Deep link cho mobile app")
                .build();
    }

    @GetMapping("/{voucherId}/email-template")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<String> getEmailTemplate(@PathVariable Integer voucherId) {
        return ApiResponse.<String>builder()
                .result(voucherSharingService.createEmailTemplate(voucherId))
                .message("Email template")
                .build();
    }
}
