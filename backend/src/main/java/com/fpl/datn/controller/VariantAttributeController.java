package com.fpl.datn.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.UpdateVariantAttributeRequest;
import com.fpl.datn.dto.request.Product.VariantAttributeRequest;
import com.fpl.datn.dto.response.Product.VariantAttributeResponse;
import com.fpl.datn.service.Product.VariantAttributeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/variantattribute")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantAttributeController {
    VariantAttributeService variantAttributeService;

    @PostMapping("/")
    public ApiResponse<Boolean> Create(@RequestBody @Valid VariantAttributeRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(variantAttributeService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VariantAttributeResponse> Detail(@PathVariable("id") int id) {
        return ApiResponse.<VariantAttributeResponse>builder()
                .code(1000)
                .result(variantAttributeService.detail(id))
                .build();
    }

    @GetMapping("/List")
    public ApiResponse<List<VariantAttributeResponse>> List() {
        return ApiResponse.<List<VariantAttributeResponse>>builder()
                .code(1000)
                .result(variantAttributeService.list())
                .build();
    }

    @GetMapping("/Get")
    public ApiResponse<PageResponse<VariantAttributeResponse>> Get(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<VariantAttributeResponse>>builder()
                .code(1000)
                .result(variantAttributeService.get(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VariantAttributeResponse> Update(
            @PathVariable("id") int id, @RequestBody UpdateVariantAttributeRequest request) {
        return ApiResponse.<VariantAttributeResponse>builder()
                .code(1000)
                .result(variantAttributeService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> Delete(@PathVariable("id") int id) {
        variantAttributeService.delete(id);
        return ApiResponse.<Void>builder().code(1000).message("Delete Success!").build();
    }
}
