package com.fpl.datn.service.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductRequest;
import com.fpl.datn.dto.request.Product.ProductVariantRequest;
import com.fpl.datn.dto.request.Product.UpdateProductRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantRequest;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.ProductMapper;
import com.fpl.datn.models.*;
import com.fpl.datn.repository.CategoryRepository;
import com.fpl.datn.repository.ProductRepository;

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
    // thêm sản phẩ
    @Transactional
    public Boolean create(ProductRequest request) {
        // 1. Kiểm tra danh mục
        if (!cateRepo.existsById(request.getCategory())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }

        // 2. Kiểm tra slug
        if (repo.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }

        // 3. Map và lưu Product (chưa gắn biến thể)
        Product product = mapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // Gắn danh mục
        Category category = cateRepo.findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        product.setCategory(category);

        // 4. Lưu product trước để có ID
        Product savedProduct = repo.save(product);
        repo.flush(); // Quan trọng để lấy productId khi sinh SKU

        // 5. Gọi ProductVariantService để tạo từng biến thể
        for (ProductVariantRequest variantRequest : request.getProductVariants()) {
            variantRequest.setProductId(savedProduct.getId()); // cần ID cha
            productVariantService.create(variantRequest); // gọi đúng logic sinh SKU
        }

        return true;
    }

    @Transactional
    public ProductResponse update(Integer id, UpdateProductRequest request) {
        Product product = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_UPDATE_NOT_EXISTED));

        // Kiểm tra slug
        if (repo.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }

        // Kiểm tra category
        if (!cateRepo.existsById(request.getCategory())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }

        // Cập nhật product chính
        mapper.updateProduct(product, request);
        product.setUpdatedAt(LocalDateTime.now());
        repo.save(product);

        // Xử lý biến thể
        List<ProductVariant> oldVariants = productVariantService.findByProductId(id);
        List<UpdateProductVariantRequest> newVariants = request.getProductVariants();

        List<Integer> newIds = newVariants.stream()
                .map(UpdateProductVariantRequest::getProductId)
                .filter(i -> i != null)
                .toList();

        // 1. Xóa biến thể không còn
        for (ProductVariant old : oldVariants) {
            if (!newIds.contains(old.getId())) {
                productVariantService.delete(old.getId());
            }
        }

        // 2. Cập nhật hoặc tạo mới biến thể
        for (UpdateProductVariantRequest variantRequest : newVariants) {
            variantRequest.setProductId(id); // Gán productId
            if (variantRequest.getId() == null) {
                // Tạo mới
                ProductVariantRequest createRequest = ProductVariantRequest.builder()
                        .productId(id)
                        .price(variantRequest.getPrice())
                        .quantity(variantRequest.getQuantity())
                        .variantName(variantRequest.getVariantName())
                        .attributeValueIds(variantRequest.getAttributeValueIds())
                        .isActive(variantRequest.getIsActive())
                        .build();
                productVariantService.create(createRequest);
            } else {
                // Cập nhật
                productVariantService.update(variantRequest.getId(), variantRequest);
            }
        }
        return mapper.toProductResponse(product);
    }
    // xem chi tiết sản phẩm
    public ProductResponse detail(Integer id) {
        Product product = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
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

        var data = pageData.getContent().stream()
                .map(product -> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                    return mapper.toProductResponse(cat);
                })
                .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public void delete(Integer id) {
        Product product = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_DELETE_NOT_EXISTED));
        if (product.getOrderDetails() != null && !product.getOrderDetails().isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_DELETE_NOT_EXISTED);
        }
        repo.delete(product);
    }

    public List<ProductResponse> search(String keyword) {
        List<Product> products = repo.searchByNameOrSku(keyword);
        return products.stream().map(mapper::toProductResponse).collect(Collectors.toList());
    }
}
