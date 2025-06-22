package com.fpl.datn.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductRequest;
import com.fpl.datn.dto.request.Product.UpdateProductRequest;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.service.Product.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @PostMapping("/")
    public ApiResponse<Boolean> Create(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(productService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> Detail(@PathVariable("id") int id) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.detail(id))
                .build();
    }

    @GetMapping("/List")
    public ApiResponse<List<ProductResponse>> List() {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(1000)
                .result(productService.list())
                .build();
    }

    @GetMapping("/Get")
    public ApiResponse<PageResponse<ProductResponse>> Get(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .code(1000)
                .result(productService.get(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> Update(@PathVariable("id") int id, @RequestBody @Valid UpdateProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.update(id, request))
                .build();

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> Delete(@PathVariable("id") int id) {
        productService.delete(id);
        return ApiResponse.<Void>builder().code(1000).message("Delete Success!").build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String keyword) {
        List<ProductResponse> results = productService.search(keyword);
        return ResponseEntity.ok(results);
    }
}