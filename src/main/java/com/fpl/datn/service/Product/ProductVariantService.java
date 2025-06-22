package com.fpl.datn.service.Product;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fpl.datn.models.*;
import com.fpl.datn.repository.*;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductVariantRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantRequest;
import com.fpl.datn.dto.response.Product.ProductVariantResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.ProductVariantMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductVariantService {
    ProductVariantRepository repo;
    ProductVariantMapper mapper;
    ProductRepository productRepo;
    VariantAttributeValueRepository attributeValueRepo;
    ProductVariantAttributeValueRepository pvaRepo;
    UploadImageRepository uploadImageRepo;
    ProductImageRepository imageRepo;

    @Transactional
    public Boolean create(ProductVariantRequest request) {
        // 1. Tìm sản phẩm cha
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        // 2. Map request → entity variant
        ProductVariant variant = mapper.toEntity(request);
        variant.setProduct(product);
        variant.setCreatedAt(LocalDateTime.now());
        variant.setUpdatedAt(LocalDateTime.now());
        // 3. Xử lý thuộc tính
        List<VariantAttributeValue> attributeValues = new ArrayList<>();
        List<ProductVariantAttributeValue> attributeLinks = new ArrayList<>();
        if (request.getAttributeValueIds() != null && !request.getAttributeValueIds().isEmpty()) {
            attributeValues = request.getAttributeValueIds().stream()
                    .map(id -> attributeValueRepo.findById(id)
                            .orElseThrow(() -> {
                                log.error("Không tìm thấy attributeValueId = {}", id);
                                return new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND);
                            }))
                    .toList();

            attributeLinks = attributeValues.stream()
                    .map(val -> ProductVariantAttributeValue.builder()
                            .productVariant(variant)
                            .attributeValue(val)
                            .build())
                    .toList();
        }
        // 4. Tạo SKU từ tên và thuộc tính
        String generatedSku = generateSkuFromVariantAndAttributes(request.getVariantName(), attributeValues);
        if (repo.existsBySku(generatedSku)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_SKU_EXISTED);
        }
        variant.setSku(generatedSku);
        // 5. Lưu variant
        ProductVariant savedVariant = repo.saveAndFlush(variant);
        // 6. Lưu liên kết thuộc tính
        if (!attributeLinks.isEmpty()) {
            pvaRepo.saveAll(attributeLinks);
        }
        // 7. Lưu ảnh nếu có (dùng imageUrl trực tiếp)
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ProductImage> images = request.getImages().stream()
                    .map(img -> ProductImage.builder()
                            .altText(img.getAltText())
                            .specDescription(img.getSpecDescription())
                            .isThumbnail(img.getIsThumbnail())
                            .sortOrder(img.getSortOrder())
                            .imageUrl(img.getImageUrl())
                            .productVariant(savedVariant)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build())
                    .toList();
            imageRepo.saveAll(images);
        }
        return true;
    }
    //Hàm helper chuẩn hoá text
    private String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }
    private String generateSkuFromVariantAndAttributes(String variantName, List<VariantAttributeValue> attributeValues) {
        // Normalize tên biến thể (variant name)
        String base = normalize(variantName);

        // Tạo phần thuộc tính: "mau-trang", "dung-luong-128gb", ...
        List<String> attributeParts = attributeValues.stream()
                .map(val -> normalize(val.getAttribute().getName()) + "-" + normalize(val.getValue()))
                .distinct()
                .sorted() // giữ thứ tự ổn định cho cùng một tập thuộc tính
                .toList();
        return base + (attributeParts.isEmpty() ? "" : "-" + String.join("-", attributeParts));
    }
    public ProductVariantResponse detail(Integer id) {
        ProductVariant productVariant = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

        return mapper.toResponse(productVariant);
    }
    public List<ProductVariantResponse> list() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    public PageResponse<ProductVariantResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);
        var data = pageData.getContent().stream()
                .map(product -> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_EXISTED));
                    return mapper.toResponse(cat);
                })
                .collect(Collectors.toList());
        return PageResponse.<ProductVariantResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }
    @Transactional
    public ProductVariantResponse update(Integer id, UpdateProductVariantRequest request) {
        // 1. Tìm variant
        ProductVariant variant = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        // 2. Cập nhật thông tin cơ bản
        mapper.toUpdate(variant, request);
        variant.setUpdatedAt(LocalDateTime.now());
        // 3. Cập nhật ảnh nếu có
        if (request.getImages() != null) {
            for (var imgRequest : request.getImages()) {
                if (imgRequest.getId() != null) {
                    // Cập nhật ảnh đã có
                    ProductImage existingImage = imageRepo.findById(imgRequest.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_DETAIL_EXISTED));
                    existingImage.setAltText(imgRequest.getAltText());
                    existingImage.setSpecDescription(imgRequest.getSpecDescription());
                    existingImage.setIsThumbnail(imgRequest.getIsThumbnail());
                    existingImage.setSortOrder(imgRequest.getSortOrder());
                    existingImage.setImageUrl(imgRequest.getImageUrl());
                    existingImage.setUpdatedAt(LocalDateTime.now());
                } else {
                    // Thêm ảnh mới
                    ProductImage newImage = ProductImage.builder()
                            .altText(imgRequest.getAltText())
                            .specDescription(imgRequest.getSpecDescription())
                            .isThumbnail(imgRequest.getIsThumbnail())
                            .sortOrder(imgRequest.getSortOrder())
                            .imageUrl(imgRequest.getImageUrl())
                            .productVariant(variant)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    imageRepo.save(newImage);
                }
            }
        }
        return mapper.toResponse(repo.save(variant));
    }




    public void delete(Integer id) {
        ProductVariant productVariant = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

        if (productVariant.getOrderDetails() != null && !productVariant.getOrderDetails().isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZE_EXCEPTION);
        }

        repo.delete(productVariant);
    }


}