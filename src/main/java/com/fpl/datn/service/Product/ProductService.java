package com.fpl.datn.service.Product;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductRequest;
import com.fpl.datn.dto.request.Product.ProductVariantRequest;
import com.fpl.datn.dto.request.Product.UpdateProductRequest;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.ProductMapper;
import com.fpl.datn.models.*;
import com.fpl.datn.repository.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository repo;
    ProductMapper mapper;
    CategoryRepository cateRepo;
    ProductVariantService productVariantService;
    ProductImageRepository imageRepo;

    // thêm sản phẩ
    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (!cateRepo.existsById(request.getCategory())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        if (repo.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        }
        if (repo.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }
        Product product = mapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Category category = cateRepo.findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        product.setCategory(category);
        Product savedProduct = repo.save(product);
        repo.flush(); // Quan trọng để lấy productId khi sinh SKU
        for (ProductVariantRequest variantRequest : request.getProductVariants()) {
            variantRequest.setProductId(savedProduct.getId()); // cần ID cha
            productVariantService.create(variantRequest); // gọi đúng logic sinh SKU
        }
        return mapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse update(Integer id, UpdateProductRequest request) {
        Product product = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (repo.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        }
        if (repo.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }
        if (!cateRepo.existsById(request.getCategory())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }

        // 4. Cập nhật Product
        mapper.updateProduct(product, request);
        product.setUpdatedAt(LocalDateTime.now());
        repo.save(product);
        Product productWithCategory =
                repo.findByIdWithCategory(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return mapper.toProductResponse(productWithCategory);
    }

    // xem chi tiết sản phẩm
    public ProductResponse detail(Integer id) {
        Product product = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return mapper.toProductResponse(product);
    }

    // xem danh sách sản phẩm
    public List<ProductResponse> list() {
        return repo.findAll().stream().map(mapper::toProductResponse).collect(Collectors.toList());
    }

    // phân trang sản phẩm
    public PageResponse<ProductResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);
        var data = pageData.getContent().stream().map(mapper::toProductResponse).collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public void delete(Integer id) {
        Product product = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.getOrderDetails() != null && !product.getOrderDetails().isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_DELETE_NOT_EXISTED);
        }
        // Kiểm tra các variant của product
        if (product.getProductVariants() != null) {
            for (ProductVariant variant : product.getProductVariants()) {
                if (variant.getOrderDetails() != null
                        && !variant.getOrderDetails().isEmpty()) {
                    throw new AppException(ErrorCode.PRODUCT_DELETE_NOT_EXISTED);
                }
            }
        }
        repo.delete(product);
    }

    public PageResponse<ProductResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = repo.searchByNameOrSku(keyword, pageable);
        List<ProductResponse> data = productPage.getContent()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .totalPages(productPage.getTotalPages())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .data(data)
                .build();
    }

}
