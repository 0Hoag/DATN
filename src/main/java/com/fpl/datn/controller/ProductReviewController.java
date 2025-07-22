package com.fpl.datn.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.ProductReviewRequest;
import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.service.ProductReviewService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/review") // ĐÃ SỬA: Đường dẫn request mapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductReviewController {
    ProductReviewService productReviewService; // ĐÃ SỬA: Tên biến service

    @GetMapping
    public ApiResponse<PageResponse<ProductReviewResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page, // ĐÃ SỬA: required = false
            @RequestParam(required = false, defaultValue = "10") int size) { // ĐÃ SỬA: required = false
        return ApiResponse.<PageResponse<ProductReviewResponse>>builder()
                .result(productReviewService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductReviewResponse> getReview(@PathVariable int id) {
        return ApiResponse.<ProductReviewResponse>builder()
                .result(productReviewService.getReview(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ProductReviewResponse> createReview(@Valid @RequestBody ProductReviewRequest request) {
        return ApiResponse.<ProductReviewResponse>builder()
                .result(productReviewService.createReview(request))
                .message("Thao tác đánh giá sản phẩm thành công!") // ĐÃ THÊM MESSAGE
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReview(@PathVariable int id) {
        productReviewService.delete(id);
        return ApiResponse.<Void>builder().message("Xóa đánh giá thành công!").build();
    }
}
