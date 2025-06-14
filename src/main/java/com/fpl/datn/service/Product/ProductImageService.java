package com.fpl.datn.service.Product;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductImageRequest;
import com.fpl.datn.dto.request.Product.UpdateProductImageRequest;
import com.fpl.datn.dto.response.Product.ProductImageResponse;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.ProductImageMapper;
import com.fpl.datn.models.ProductImage;
import com.fpl.datn.models.ProductVariant;
import com.fpl.datn.repository.ProductImageRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageService {
    ProductImageRepository repo;
    ProductImageMapper mapper;
    ProductVariantRepository repoPRV;

    // Create
    public Boolean create(ProductImageRequest request) {
        if (!repoPRV.existsById(request.getProductVariantId())) {
        throw new AppException(ErrorCode.PRODUCT_IMAGE_CREATE_EXISTED);
        }
        ProductImage productImage = mapper.toEntity(request);
        productImage.setCreatedAt(LocalDateTime.now());
        productImage.setUpdatedAt(LocalDateTime.now());
        // gán biến thể sản phẩm
        ProductVariant variant = repoPRV.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED));
        productImage.setProductVariant(variant);
        //lưu db
        repo.save(productImage);
        return true;
    }
    // Detail
    public ProductImageResponse detail(Integer id) {
        ProductImage productImage = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_DETAIL_EXISTED));
        return mapper.toResponse(productImage);
    }
    // List
    public List<ProductImageResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    //Phân trang
    public PageResponse<ProductImageResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream()
                .map(product-> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_DETAIL_EXISTED));
                    return mapper.toResponse(cat);
                })
                .collect(Collectors.toList());

        return PageResponse.<ProductImageResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }
    public ProductImageResponse update(Integer id, UpdateProductImageRequest request) {
        ProductImage productImage = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_UPDATE_EXISTED));
        mapper.update(productImage, request);
        productImage.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(repo.save(productImage));
    }
    public void delete(Integer id) {
        ProductImage productImage = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_DELETE_EXISTED));
        repo.delete(productImage);
    }
}
