package com.fpl.datn.controller;

import com.cloudinary.Api;
import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductImageRequest;
import com.fpl.datn.dto.request.Product.UpdateProductImageRequest;
import com.fpl.datn.dto.response.Product.ProductImageResponse;
import com.fpl.datn.dto.response.Product.ProductVariantResponse;
import com.fpl.datn.service.Product.ProductImageService;
import com.fpl.datn.service.Product.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productimage")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageController {
    ProductImageService prdimageService;

    @PostMapping("/")
    public ApiResponse<Boolean> Create(@RequestBody @Valid ProductImageRequest reqeust) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(prdimageService.create(reqeust))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<ProductImageResponse> Detail(@PathVariable("id") int id) {
        return ApiResponse.<ProductImageResponse>builder()
                .code(1000)
                .result(prdimageService.detail(id))
                .build();
    }
    @GetMapping("/List")
    public ApiResponse<List<ProductImageResponse>> List(){
        return ApiResponse.<List<ProductImageResponse>>builder()
                .code(1000)
                .result(prdimageService.list())
                .build();
    }
    @GetMapping("Get")
    public ApiResponse<PageResponse<ProductImageResponse>> Get(
            @RequestParam(value = "page",required = false,defaultValue = "1") int page,
            @RequestParam(value = "size",required = false,defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductImageResponse>>builder()
                .code(1000)
                .result(prdimageService.get(page, size))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<ProductImageResponse> Update(
            @PathVariable("id") int id, @RequestBody UpdateProductImageRequest request) {
        return ApiResponse.<ProductImageResponse>builder()
                .code(1000)
                .result(prdimageService.update(id,request))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> Delete(@PathVariable("id") int id) {
    prdimageService.delete(id);
    return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete Sucess!")
            .build();
    }
}
