package com.fpl.datn.service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.UpdateProductRequest;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.ProductMapper;
import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;
import com.fpl.datn.repository.CategoryRepository;
import com.fpl.datn.repository.ProductRepository;
import com.fpl.datn.dto.request.Product.ProductRequest;
import com.fpl.datn.dto.response.Product.ProductResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository repo;
    ProductMapper mapper;
    CategoryRepository cateRepo;
// thêm sản phẩm
    public Boolean create(ProductRequest request) {
        if (!cateRepo.existsById(request.getCategory())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        if (repo.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }
        Product product = mapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        repo.save(product);
        return true;
    }
    // xem chi tiết sản phẩm
    public ProductResponse detail(Integer id) {
        Product product = repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        return mapper.toProductResponse(product);
    }
    // xem danh sách sản phẩm
    public List<ProductResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
    // phân trang sản phẩm
    public PageResponse<ProductResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream()
                .map(product-> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
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
    public ProductResponse update(Integer id, UpdateProductRequest request) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_UPDATE_NOT_EXISTED));

        // Kiểm tra slug có bị trùng ở sản phẩm khác
        if (repo.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.PRODUCT_SLUG_EXISTED);
        }

        // Kiểm tra category có tồn tại
        if (!cateRepo.existsById(request.getCategory())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }

        mapper.updateProduct(product, request);
        product.setUpdatedAt(LocalDateTime.now());

        return mapper.toProductResponse(repo.save(product));
    }

    public void delete(Integer id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_DELETE_NOT_EXISTED));
            if(product.getOrderDetails() != null && !product.getOrderDetails().isEmpty()){
                throw new AppException(ErrorCode.PRODUCT_DELETE_NOT_EXISTED);
            }
            repo.delete(product);
    }
}

