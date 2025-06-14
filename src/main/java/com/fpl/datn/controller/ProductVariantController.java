package com.fpl.datn.controller;

import com.cloudinary.Api;
import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductVariantRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantRequest;
import com.fpl.datn.dto.response.Product.ProductVariantResponse;
import com.fpl.datn.models.ProductVariant;
import com.fpl.datn.service.Product.ProductVariantService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productvariant")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantController {
    ProductVariantService productVariantService;

    @PostMapping("/")
    public ApiResponse<Boolean> Create(@RequestBody  @Valid ProductVariantRequest request) {
       return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(productVariantService.create(request))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<ProductVariantResponse> Detail(@PathVariable("id") int id){
        return ApiResponse.<ProductVariantResponse>builder()
                .code(1000)
                .result(productVariantService.detail(id))
                .build();
    }
    @GetMapping("/List")
    public ApiResponse<List<ProductVariantResponse>> List(){
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .code(1000)
                .result(productVariantService.list())
                .build();
    }
    @GetMapping("/Get")
    public ApiResponse<PageResponse<ProductVariantResponse>> Get(
            @RequestParam(value= "page", required = false, defaultValue = "1") int page,
            @RequestParam(value= "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ProductVariantResponse>>builder()
                .code(1000)
                .result(productVariantService.get(page, size))
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<ProductVariantResponse> Update(
            @PathVariable("id") int id, @RequestBody UpdateProductVariantRequest request) {
        return ApiResponse.<ProductVariantResponse>builder()
                .code(1000)
                .result(productVariantService.update(id,request))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> Delete(@PathVariable("id") int id) {
        productVariantService.delete(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete success!")
                .build();
    }
}
