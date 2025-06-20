package com.fpl.datn.controller;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductImageRequest;
import com.fpl.datn.dto.request.Product.UpdateProductImageRequest;
import com.fpl.datn.dto.response.Product.ProductImageResponse;
import com.fpl.datn.service.Product.ProductImageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/productimage")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageController {

    ProductImageService prdimageService;

    @PostMapping("/")
    public ApiResponse<Boolean> create(@ModelAttribute @Valid ProductImageRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(prdimageService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductImageResponse> detail(@PathVariable("id") int id) {
        return ApiResponse.<ProductImageResponse>builder()
                .code(1000)
                .result(prdimageService.detail(id))
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<List<ProductImageResponse>> list() {
        return ApiResponse.<List<ProductImageResponse>>builder()
                .code(1000)
                .result(prdimageService.list())
                .build();
    }

    @GetMapping("/get")
    public ApiResponse<PageResponse<ProductImageResponse>> get(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductImageResponse>>builder()
                .code(1000)
                .result(prdimageService.get(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductImageResponse> update(
            @PathVariable("id") int id,
            @RequestBody @Valid UpdateProductImageRequest request) {
        return ApiResponse.<ProductImageResponse>builder()
                .code(1000)
                .result(prdimageService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") int id) {
        prdimageService.delete(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete Success!")
                .build();
    }
}
