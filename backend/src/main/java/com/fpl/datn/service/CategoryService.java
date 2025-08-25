package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.ActivityRequest;
import com.fpl.datn.dto.request.CategoryRequest;
import com.fpl.datn.dto.request.UpdateCategoryRequest;
import com.fpl.datn.dto.response.CategoryResponse;
import com.fpl.datn.enums.ActionActicityLog;
import com.fpl.datn.enums.ActionActicityModule;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.CategoryMapper;
import com.fpl.datn.models.Category;
import com.fpl.datn.repository.CategoryRepository;
import com.fpl.datn.service.build.CategoryBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryMapper mapper;
    CategoryRepository repo;
    CategoryBuilder builder;
    ActivitylogService activitylogService;

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public Boolean createCategory(CategoryRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new AppException(ErrorCode.MISSING_INPUT);
        }
        if (repo.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_NAME_EXISTED);
        }
        if (repo.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.CATEGORY_SLUG_EXISTED);
        }

        Category category = mapper.toCategory(request);

        if (request.getParent() != 0) {
            Category parent = repo.findById(request.getParent())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            if (builder.wouldCreateCircularReference(null, parent.getId())) {
                throw new AppException(ErrorCode.CIRCULAR_REFERENCE_NOT_ALLOWED);
            }
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        try {
            repo.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.UNKNOWN_ERROR);
        }

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Create)
                .description("Tạo danh mục: " + category.getName())
                .module(ActionActicityModule.Category)
                .objectID(category.getId())
                .build());

        return true;
    }

    public CategoryResponse detail(Integer id) {
        Category category = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return mapper.toCategoryResponse(category);
    }

    public CategoryResponse detail(String slug) {
        Category category = repo.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return mapper.toCategoryResponse(category);
    }
    // hien thi cho user
    public List<CategoryResponse> list() {
        var list = repo.findAllByIsShowTrue().stream()
                .map(mapper::toCategoryResponse)
                .collect(Collectors.toList());

        return list;
    }
    // dung cho admin
    public PageResponse<CategoryResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream()
                .map(category -> mapper.toCategoryResponse(repo.findById(category.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED))))
                .collect(Collectors.toList());

        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public CategoryResponse update(Integer id, UpdateCategoryRequest request) {
        Category category = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Integer newParentId = request.getParent();

        if (newParentId != null && newParentId != 0) {
            Category parent =
                    repo.findById(newParentId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            if (builder.wouldCreateCircularReference(id, newParentId)) {
                throw new AppException(ErrorCode.CIRCULAR_REFERENCE_NOT_ALLOWED);
            }

            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        mapper.update(category, request);
        category.setUpdatedAt(LocalDateTime.now());

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Update)
                .description("Cập nhập danh mục: " + category.getName())
                .module(ActionActicityModule.Category)
                .objectID(id)
                .build());

        return mapper.toCategoryResponse(repo.save(category));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void delete(Integer id) {
        Category category = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_HAS_CHILDREN);
        }
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Delete)
                .description("Xóa danh mục: " + category.getName())
                .module(ActionActicityModule.Category)
                .objectID(id)
                .build());

        repo.delete(category);
    }

    public PageResponse<CategoryResponse> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Category> pageData = repo.findByNameContainingIgnoreCase(keyword, pageable);
        var data = pageData.stream().map(mapper::toCategoryResponse).toList();
        return PageResponse.<CategoryResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }
}
