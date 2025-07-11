package com.fpl.datn.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.service.ProductReviewService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductReviewController {
    ProductReviewService productReviewService;

    @GetMapping
    ApiResponse<PageResponse<ProductReviewResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductReviewResponse>>builder()
                .result(productReviewService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ProductReviewResponse> getProductReview(@PathVariable int id) {
        return ApiResponse.<ProductReviewResponse>builder()
                .result(productReviewService.getReview(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable int id) {
        productReviewService.delete(id);
        return ApiResponse.<Void>builder().message("Delete success!").build();
    }
}
