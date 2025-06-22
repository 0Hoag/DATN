package com.fpl.datn.service.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductImageRequest;
import com.fpl.datn.dto.request.Product.UpdateProductImageRequest;
import com.fpl.datn.dto.response.Product.ProductImageResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.ProductImageMapper;
import com.fpl.datn.models.ProductImage;
import com.fpl.datn.models.ProductVariant;
import com.fpl.datn.repository.ProductImageRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.UploadImageRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageService {

    ProductImageRepository repo;
    ProductImageMapper mapper;
    ProductVariantRepository repoPRV;
    UploadImageRepository uploadImageRepo; // ✅ repo ảnh đã upload trước

    public Boolean create(ProductImageRequest request) {
        ProductVariant variant = repoPRV.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

        ProductImage productImage = mapper.toEntity(request);
        productImage.setProductVariant(variant);
        productImage.setCreatedAt(LocalDateTime.now());
        productImage.setUpdatedAt(LocalDateTime.now());

        repo.save(productImage);
        return true;
    }

    public ProductImageResponse detail(Integer id) {
        ProductImage productImage =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_ID_REQUIRED));
        return mapper.toResponse(productImage);
    }

    public List<ProductImageResponse> list() {
        return repo.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public PageResponse<ProductImageResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        List<ProductImageResponse> data =
                pageData.getContent().stream().map(mapper::toResponse).collect(Collectors.toList());

        return PageResponse.<ProductImageResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public ProductImageResponse update(Integer id, UpdateProductImageRequest request) {
        ProductImage productImage =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_UPLOAD_ID_REQUIRED));

        mapper.update(productImage, request); // ✅ Ánh xạ các trường cơ bản
        productImage.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repo.save(productImage));
    }

    public void delete(Integer id) {
        ProductImage productImage =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_ID_REQUIRED));
        repo.delete(productImage);
    }
}
