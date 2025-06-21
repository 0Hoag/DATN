package com.fpl.datn.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductVariantAttributeValueRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantAttributeValueRequest;
import com.fpl.datn.dto.response.Product.ProductVariantAttributeValueResponse;
import com.fpl.datn.service.Product.ProductVariantAttributeValueService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/productvav")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantAttributeValueController {
    ProductVariantAttributeValueService prdvavService;

    @PostMapping("/")
    public ApiResponse<Boolean> Create(@RequestBody @Valid ProductVariantAttributeValueRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(prdvavService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductVariantAttributeValueResponse> Detail(@PathVariable("id") int id) {
        return ApiResponse.<ProductVariantAttributeValueResponse>builder()
                .code(1000)
                .result(prdvavService.detail(id))
                .build();
    }

    @GetMapping("/List")
    public ApiResponse<List<ProductVariantAttributeValueResponse>> List() {
        return ApiResponse.<List<ProductVariantAttributeValueResponse>>builder()
                .code(1000)
                .result(prdvavService.list())
                .build();
    }

    @GetMapping("Get")
    public ApiResponse<PageResponse<ProductVariantAttributeValueResponse>> Get(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductVariantAttributeValueResponse>>builder()
                .code(1000)
                .result(prdvavService.get(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductVariantAttributeValueResponse> update(
            @PathVariable("id") int id, @RequestBody UpdateProductVariantAttributeValueRequest request) {
        return ApiResponse.<ProductVariantAttributeValueResponse>builder()
                .code(1000)
                .result(prdvavService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> Delete(@PathVariable("id") int id) {
        prdvavService.delete(id);
        return ApiResponse.<Void>builder().code(1000).message("Delete Sucess!").build();
    }
}
