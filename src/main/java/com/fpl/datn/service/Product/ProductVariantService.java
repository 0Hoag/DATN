package com.fpl.datn.service.Product;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductRequest;
import com.fpl.datn.dto.request.Product.ProductVariantRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantRequest;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.dto.response.Product.ProductVariantResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.ProductVariantMapper;
import com.fpl.datn.models.Product;
import com.fpl.datn.models.ProductVariant;
import com.fpl.datn.models.ProductVariantAttributeValue;
import com.fpl.datn.models.VariantAttributeValue;
import com.fpl.datn.repository.ProductRepository;
import com.fpl.datn.repository.ProductVariantAttributeValueRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.VariantAttributeValueRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public Boolean create(ProductVariantRequest request) {
        // 1. Kiểm tra Product có tồn tại
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // 2. Tạo ProductVariant (chưa gán SKU)
        ProductVariant productVariant = mapper.toEntity(request);
        productVariant.setProduct(product);
        productVariant.setCreatedAt(LocalDateTime.now());
        productVariant.setUpdatedAt(LocalDateTime.now());

        // 3. Chuẩn bị thuộc tính và tạo SKU
        List<String> attributeValueTexts = new ArrayList<>();
        List<ProductVariantAttributeValue> attributeLinks = new ArrayList<>();

        if (request.getAttributeValueIds() != null && !request.getAttributeValueIds().isEmpty()) {
            for (Integer valueId : request.getAttributeValueIds()) {
                VariantAttributeValue attrValue = attributeValueRepo.findById(valueId)
                        .orElseThrow(() -> {
                            log.error("❌ Không tìm thấy attributeValueId = {}", valueId);
                            return new AppException(ErrorCode.VARIANT_DETAIL_EXISTED);
                        });

                // Normalize giá trị thuộc tính để tạo SKU
                String normalizedValue = attrValue.getValue().toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", "") // bỏ ký tự đặc biệt
                        .replaceAll("\\s+", "-");       // thay khoảng trắng bằng gạch

                attributeValueTexts.add(normalizedValue);

                // Tạo liên kết thuộc tính - biến thể
                attributeLinks.add(ProductVariantAttributeValue.builder()
                        .productVariant(productVariant)
                        .attributeValue(attrValue)
                        .build());
            }
        }

        // 4. Tạo SKU tự động
        String normalizedProductName = product.getName().toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");

        String generatedSku = normalizedProductName;
        if (!attributeValueTexts.isEmpty()) {
            generatedSku += "-" + String.join("-", attributeValueTexts);
        }

        // 5. Kiểm tra SKU đã tồn tại
        if (repo.existsBySku(generatedSku)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_SKU_EXISTED);
        }

        // Gán SKU vào biến thể
        productVariant.setSku(generatedSku);

        // 6. Lưu ProductVariant và flush để lấy ID nếu cần
        ProductVariant savedVariant = repo.save(productVariant);
        repo.flush();

        // 7. Lưu thuộc tính liên kết
        if (!attributeLinks.isEmpty()) {
            pvaRepo.saveAll(attributeLinks);
        }

        log.info("✅ Tạo biến thể thành công với SKU: {}", generatedSku);
        return true;
    }

    public List<ProductVariant> findByProductId(Integer productId) {
        return repo.findAllByProduct_Id(productId);
    }


    public ProductVariantResponse detail(Integer id) {
        ProductVariant productVariant = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED));
        return mapper.toResponse(productVariant);
    }

    public List<ProductVariantResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<ProductVariantResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream()
                .map(product -> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED));
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

    public ProductVariantResponse update(Integer id, UpdateProductVariantRequest request) {
        ProductVariant productVariant = repo.findById(id)
                .orElseThrow(()
                        -> new AppException(ErrorCode.PRODUCT_UPDATE_NOT_EXISTED));
            mapper.toUpdate(productVariant, request);
            productVariant.setUpdatedAt(LocalDateTime.now());
        return mapper.toResponse(repo.save(productVariant));
    }
    public void delete(Integer id) {
        ProductVariant productVariant = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_DELETE_VARIANT_EXISTED));
        repo.delete(productVariant);
    }
}
