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
import com.fpl.datn.models.UploadImage;
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
        // ✅ 1. Kiểm tra ProductVariant có tồn tại không
        ProductVariant variant = repoPRV.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_ID_NOT_EXISTED));

        // ✅ 2. Kiểm tra ảnh đã upload có tồn tại không
        UploadImage uploadImage = uploadImageRepo.findById(request.getUploadImageId())
                .orElseThrow(() -> new AppException(ErrorCode.UPLOAD_IMAGE_NOT_FOUND));

        // ✅ 3. Map dữ liệu sang entity và gán quan hệ
        ProductImage productImage = mapper.toEntity(request);
        productImage.setUploadImage(uploadImage); // liên kết entity
        productImage.setProductVariant(variant);
        productImage.setCreatedAt(LocalDateTime.now());
        productImage.setUpdatedAt(LocalDateTime.now());

        repo.save(productImage);
        return true;
    }

    public ProductImageResponse detail(Integer id) {
        ProductImage productImage = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_DETAIL_EXISTED));
        return mapper.toResponse(productImage);
    }

    public List<ProductImageResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<ProductImageResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        List<ProductImageResponse> data = pageData.getContent().stream()
                .map(mapper::toResponse)
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
