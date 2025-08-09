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
                .message("Lấy danh sách đánh giá sản phẩm thành công!")
                .build();
    }

    // ===== FIX: CUSTOMER + ADMIN có thể tạo review =====
    @PostMapping
    public ApiResponse<ProductReviewResponse> createReview(@Valid @RequestBody ProductReviewRequest request) {
        return ApiResponse.<ProductReviewResponse>builder()
                .result(productReviewService.createReview(request))
                .message("Đánh giá sản phẩm thành công!")
                .build();
    }

    // ===== ADMIN: XÓA ĐÁNH GIÁ =====
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ApiResponse<Void> deleteReview(@PathVariable int id) {
        productReviewService.deleteReview(id);
        return ApiResponse.<Void>builder().message("Xóa đánh giá thành công!").build();
    }

    @PostMapping("/hideReview/{id}")
    public ApiResponse<Void> hideReview(@PathVariable int id) {
        productReviewService.hideReview(id);
        return ApiResponse.<Void>builder().message("Ẩn đánh giá thành công!").build();
    }

    // ===== FIX: CUSTOMER + ADMIN có thể xóa review của mình =====
    @DeleteMapping("/my/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ApiResponse<Void> deleteMyReview(@PathVariable int id) {
        productReviewService.deleteMyReview(id);
        return ApiResponse.<Void>builder()
                .message("Xóa đánh giá của bạn thành công!")
                .build();
    }

    // ===== MỚI: THỐNG KÊ ĐÁNH GIÁ THEO SẢN PHẨM =====
    @GetMapping("/stats/{productId}")
    public ApiResponse<ProductReviewStatsResponse> getReviewStats(@PathVariable Integer productId) {
        return ApiResponse.<ProductReviewStatsResponse>builder()
                .result(productReviewService.getReviewStats(productId))
                .message("Lấy thống kê đánh giá thành công!")
                .build();
    }

    // ===== FIX: CUSTOMER + ADMIN có thể check review status =====
    @GetMapping("/check-reviewed/{productId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ApiResponse<Boolean> hasUserReviewedProduct(@PathVariable Integer productId) {
        return ApiResponse.<Boolean>builder()
                .result(productReviewService.hasUserReviewedProduct(productId))
                .message("Kiểm tra trạng thái đánh giá thành công!")
                .build();
    }

    // ===== FIX: CUSTOMER + ADMIN có thể check purchase status =====
    @GetMapping("/check-purchased/{productId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ApiResponse<Boolean> hasUserPurchasedProduct(@PathVariable Integer productId) {
        return ApiResponse.<Boolean>builder()
                .result(productReviewService.hasUserPurchasedProduct(productId))
                .message("Kiểm tra trạng thái mua hàng thành công!")
                .build();
    }
}
