package com.fpl.datn.service.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.fpl.datn.models.Product;
import com.fpl.datn.models.ProductVariant;
import com.fpl.datn.models.ProductVariantAttributeValue;
import com.fpl.datn.models.VariantAttributeValue;
import com.fpl.datn.repository.ProductRepository;
import com.fpl.datn.repository.ProductVariantAttributeValueRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.VariantAttributeValueRepository;

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

    @Transactional
    public Boolean create(ProductVariantRequest request) {
        // 1. Tìm sản phẩm
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ID_NOT_EXISTED));
        // 2. Map request → entity
        ProductVariant variant = mapper.toEntity(request);
        variant.setCreatedAt(LocalDateTime.now());
        variant.setUpdatedAt(LocalDateTime.now());
        // 3. Xử lý thuộc tính & tạo liên kết
        List<VariantAttributeValue> attributeValues = new ArrayList<>();
        List<String> attributeValueTexts = new ArrayList<>();
        List<ProductVariantAttributeValue> attributeLinks = new ArrayList<>();
        if (request.getAttributeValueIds() != null && !request.getAttributeValueIds().isEmpty()) {
            attributeValues = request.getAttributeValueIds().stream()
                    .map(id -> attributeValueRepo.findById(id)
                            .orElseThrow(() -> {
                                log.error("Không tìm thấy attributeValueId = {}", id);
                                return new AppException(ErrorCode.VARIANT_VALUEID_NOT_FOUND);
                            }))
                    .toList();
            attributeValueTexts = attributeValues.stream()
                    .map(val -> normalize(val.getValue()))
                    .toList();
            attributeLinks = attributeValues.stream()
                    .map(val -> ProductVariantAttributeValue.builder()
                            .productVariant(variant)
                            .attributeValue(val)
                            .build())
                    .toList();
        }
        // 4. Tạo SKU
        String generatedSku = generateSku(product.getName(), attributeValueTexts);
        if (repo.existsBySku(generatedSku)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_SKU_EXISTED);
        }
        variant.setSku(generatedSku);
        // 5. Lưu Variant
        ProductVariant savedVariant = repo.saveAndFlush(variant);
        // 6. Lưu liên kết thuộc tính
        if (!attributeLinks.isEmpty()) {
            pvaRepo.saveAll(attributeLinks);
        }
        return true;
    }
    //Hàm helper chuẩn hoá text
    private String normalize(String value) {
        return value.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }
    //Hàm helper tạo SKU
    private String generateSku(String productName, List<String> attributes) {
        String base = normalize(productName);
        return attributes.isEmpty() ? base : base + "-" + String.join("-", attributes);
    }

    public List<ProductVariant> findByProductId(Integer productId) {
        return repo.findAllByProduct_Id(productId);
    }

    public ProductVariantResponse detail(Integer id) {
        ProductVariant productVariant =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_ID_NOT_EXISTED));
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
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_ID_NOT_EXISTED));
        // 2. Cập nhật thông tin cơ bản
        mapper.toUpdate(variant, request);
        variant.setUpdatedAt(LocalDateTime.now());
        // 3. Xử lý attributeValueIds (nếu có)
        List<Integer> newAttrIds = request.getAttributeValueIds();
        if (newAttrIds != null && !newAttrIds.isEmpty()) {
            // Lấy danh sách liên kết hiện tại
            List<ProductVariantAttributeValue> existingLinks = pvaRepo.findAllByProductVariantId(id);
            Set<Integer> existingAttrIds = existingLinks.stream()
                    .map(link -> link.getAttributeValue().getId())
                    .collect(Collectors.toSet());
            // Tìm các attribute mới cần thêm
            List<VariantAttributeValue> newAttrValues = newAttrIds.stream()
                    .filter(attrId -> !existingAttrIds.contains(attrId)) // chưa có thì thêm
                    .map(attrId -> attributeValueRepo.findById(attrId)
                            .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUEID_NOT_FOUND)))
                    .toList();
            // Tạo liên kết mới
            List<ProductVariantAttributeValue> newLinks = newAttrValues.stream()
                    .map(attr -> ProductVariantAttributeValue.builder()
                            .productVariant(variant)
                            .attributeValue(attr)
                            .build())
                    .toList();
            if (!newLinks.isEmpty()) {
                pvaRepo.saveAll(newLinks);
            }
            // Cập nhật lại SKU nếu có thuộc tính mới
            String base = normalize(variant.getProduct().getName());
            List<String> allAttributeTexts = Stream.concat(
                            existingLinks.stream().map(link -> normalize(link.getAttributeValue().getValue())),
                            newAttrValues.stream().map(val -> normalize(val.getValue()))
                    )
                    .distinct() // tránh trùng
                    .toList();
            String newSku = base + "-" + String.join("-", allAttributeTexts);
            if (!newSku.equals(variant.getSku()) && repo.existsBySku(newSku)) {
                throw new AppException(ErrorCode.PRODUCT_VARIANT_SKU_EXISTED);
            }
            variant.setSku(newSku);
        }
        // 4. Lưu và trả về
        return mapper.toResponse(repo.save(variant));
    }
    public void delete(Integer id) {
        ProductVariant productVariant =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_DELETE_VARIANT_EXISTED));
        repo.delete(productVariant);
    }
}
