package com.fpl.datn.controller;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.CategoryRequest;
import com.fpl.datn.dto.request.UpdateCategoryRequest;
import com.fpl.datn.dto.response.CategoryResponse;
import com.fpl.datn.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;


    @PostMapping("/")
    public ApiResponse<Boolean> Create(@RequestBody @Valid CategoryRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .result(categoryService.createCategory(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> Detail(@PathVariable("id") int id) {
        return ApiResponse.<CategoryResponse>builder()
                .code(1000)
                .result(categoryService.detail(id))
                .build();
    }

    @GetMapping("/List")
    public ApiResponse<List<CategoryResponse>> List() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(1000)
                .result(categoryService.list())
                .build();
    }

    @GetMapping("/Get")
    public ApiResponse<PageResponse<CategoryResponse>> Get(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .code(1000)
                .result(categoryService.get(page, size))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> Update(
            @PathVariable("id") int id, @RequestBody UpdateCategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(1000)
                .result(categoryService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> Delete(@PathVariable("id") int id) {
        categoryService.delete(id);
        return ApiResponse.<Void>builder().code(1000).message("Delete success!").build();
    }
}