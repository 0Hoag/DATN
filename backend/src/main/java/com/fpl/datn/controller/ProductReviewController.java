package com.fpl.datn.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
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
import com.fpl.datn.dto.request.ProductReviewRequest;
import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.dto.response.ProductReviewStatsResponse;
import com.fpl.datn.service.ProductReviewService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductReviewController {
    ProductReviewService productReviewService;

    // ===== ADMIN: XEM TẤT CẢ ĐÁNH GIÁ =====
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ApiResponse<PageResponse<ProductReviewResponse>> getAllReviews(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductReviewResponse>>builder()
                .result(productReviewService.getAllReviews(page, size))
                .message("Lấy danh sách tất cả đánh giá thành công!")
                .build();
    }

    // ===== PUBLIC: XEM ĐÁNH GIÁ THEO SẢN PHẨM =====
    @GetMapping("/product/{productId}")
    public ApiResponse<PageResponse<ProductReviewResponse>> getReviewsByProduct(
            @PathVariable Integer productId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductReviewResponse>>builder()
                .result(productReviewService.getReviewsByProduct(productId, page, size))
                .build();
    }

    // ===== FIX: CUSTOMER + ADMIN có thể tạo review =====
    @PostMapping
    public ApiResponse<ProductReviewResponse> createReview(@Valid @RequestBody ProductReviewRequest request) {
        return ApiResponse.<ProductReviewResponse>builder()
                .result(productReviewService.createReview(request))
                .build();
    }

    // ===== ADMIN: XÓA ĐÁNH GIÁ =====
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ApiResponse<Void> deleteReview(@PathVariable int id) {
        productReviewService.deleteReview(id);
        return ApiResponse.<Void>builder().message("Xóa đánh giá thành công!").build();
    }


    @GetMapping("/search")
    public ApiResponse<PageResponse<ProductReviewResponse>> searchReviewsByProductName(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductReviewResponse>>builder()
                .result(productReviewService.searchReviewsByProductName(productName, page, size))
                .build();
    }

}
