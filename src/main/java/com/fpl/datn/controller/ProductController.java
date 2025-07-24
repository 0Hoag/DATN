package com.fpl.datn.controller;

import java.math.BigDecimal;
import java.util.List;

import com.cloudinary.Api;
import com.fpl.datn.dto.request.Product.ProductFilterRequest;
import com.fpl.datn.dto.response.Product.ProductSaleResponse;
import com.fpl.datn.models.Product;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
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
    public ApiResponse<ProductResponse> Create(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> detail(@PathVariable("id") int id) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.detail(id))
                .build();
    }

    @GetMapping("/detail/{slug}")
    public ApiResponse<ProductResponse> detailbySlug(@PathVariable String slug) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .result(productService.detailbySlug(slug))
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
    public ApiResponse<ProductResponse> Update(
            @PathVariable("id") int id, @RequestBody @Valid UpdateProductRequest request) {
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
    public ApiResponse<PageResponse<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .code(1000)
                .result(productService.search(keyword, page, size))
                .build();
    }


    @GetMapping("/sale")
    public ApiResponse<List<ProductSaleResponse>> getSaleProductsSimple(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<List<ProductSaleResponse>>builder()
                .code(1000)
                .result(productService.getSaleProductsSimple(page, size))
                .build();
    }
    @GetMapping("/filter")
    public ApiResponse<PageResponse<ProductResponse>> filterProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) List<String> brands, // ?brands=Apple&brands=Samsung
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponse> result = productService.filterProducts(categoryId, brands, minPrice, maxPrice, page, size);

        PageResponse<ProductResponse> pageResponse = PageResponse.<ProductResponse>builder()
                .currentPage(result.getNumber())
                .totalPages(result.getTotalPages())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .data(result.getContent())
                .build();

        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .code(1000)
                .result(pageResponse)
                .build();
    }

}
