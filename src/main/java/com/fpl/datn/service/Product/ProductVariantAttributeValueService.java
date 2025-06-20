package com.fpl.datn.service.Product;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.ProductVariantAttributeValueRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantAttributeValueRequest;
import com.fpl.datn.dto.response.Product.ProductVariantAttributeValueResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.ProductVariantAttributeValueMapper;
import com.fpl.datn.models.ProductVariant;
import com.fpl.datn.models.ProductVariantAttributeValue;
import com.fpl.datn.models.VariantAttributeValue;
import com.fpl.datn.repository.ProductVariantAttributeValueRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.VariantAttributeValueRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductVariantAttributeValueService {

    ProductVariantAttributeValueRepository repo;
    ProductVariantAttributeValueMapper mapper;
    ProductVariantRepository productVariantRepo;
    VariantAttributeValueRepository variantAttributeValueRepo;

    // CREATE
    public Boolean create(ProductVariantAttributeValueRequest request) {
        ProductVariant variant = productVariantRepo.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED));

        VariantAttributeValue value = variantAttributeValueRepo.findById(request.getAttributeValueId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_DETAIL_EXISTED));

        boolean exists = repo.existsByProductVariantIdAndAttributeValueId(
                request.getProductVariantId(), request.getAttributeValueId());
        if (exists) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_EXISTED);
        }

        ProductVariantAttributeValue entity = mapper.toEntity(request);
        entity.setProductVariant(variant);
        entity.setAttributeValue(value);
        repo.save(entity);
        return true;
    }

    // UPDATE
    public ProductVariantAttributeValueResponse update(Integer id, UpdateProductVariantAttributeValueRequest request) {
        ProductVariantAttributeValue entity = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_EXISTED));

        ProductVariant variant = productVariantRepo.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_EXISTED));

        VariantAttributeValue attributeValue = variantAttributeValueRepo.findById(request.getAttributeValueId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_DETAIL_EXISTED));

        // Giả sử mapper.update(...) có tồn tại, như ProductImageService
        mapper.update(entity, request); // Nếu bạn có phương thức này trong mapper

        entity.setProductVariant(variant);
        entity.setAttributeValue(attributeValue);

        return mapper.toResponse(repo.save(entity));
    }



    // DELETE
    public Boolean delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_EXISTED);
        }
        repo.deleteById(id);
        return true;
    }
    public ProductVariantAttributeValueResponse detail(Integer id) {
        ProductVariantAttributeValue entity = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_EXISTED));
        return mapper.toResponse(entity);
    }
    public List<ProductVariantAttributeValueResponse> list() {
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    public PageResponse<ProductVariantAttributeValueResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream()
                .map(product-> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_DETAIL_EXISTED));
                    return mapper.toResponse(cat);
                })
                .collect(Collectors.toList());

        return PageResponse.<ProductVariantAttributeValueResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }
}

