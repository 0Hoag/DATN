package com.fpl.datn.service.Product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.UpdateVariantAttributeRequest;
import com.fpl.datn.dto.request.Product.VariantAttributeRequest;
import com.fpl.datn.dto.response.Product.VariantAttributeResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.VariantAttributeMapper;
import com.fpl.datn.models.VariantAttribute;
import com.fpl.datn.models.VariantAttributeValue;
import com.fpl.datn.repository.VariantAttributeRepository;
import com.fpl.datn.repository.VariantAttributeValueRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VariantAttributeService {
    VariantAttributeMapper mapper;
    VariantAttributeRepository repo;
    VariantAttributeValueRepository valueRepo;

    public Boolean create(VariantAttributeRequest request) {
        VariantAttribute existing = repo.findByName(request.getName()).orElse(null);
        VariantAttribute entity = mapper.toEntity(request);
        if (existing != null) {
            entity.setId(existing.getId());
        }
        if (entity.getValues() != null && !entity.getValues().isEmpty()) {
            Set<String> seen = new HashSet<>();
            for (VariantAttributeValue val : entity.getValues()) {
                String normalized = val.getValue().trim();
                // Kiểm tra trùng trong request
                if (!seen.add(normalized.toLowerCase())) {
                    throw new AppException(ErrorCode.DUPLICATE_ATTRIBUTE_VALUE);
                }
                // Kiểm tra trùng trong DB nếu đang update
                if (existing != null
                        && valueRepo.existsByValueIgnoreCaseAndAttribute_Id(normalized, existing.getId())) {
                    throw new AppException(ErrorCode.ATTRIBUTE_VALUE_ALREADY_EXISTS);
                }
                val.setAttribute(entity);
            }
        }
        repo.save(entity);
        return true;
    }

    public VariantAttributeResponse detail(Integer id) {
        VariantAttribute variantAttribute =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND));
        return mapper.toResponse(variantAttribute);
    }

    public List<VariantAttributeResponse> list() {
        return repo.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public PageResponse<VariantAttributeResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream()
                .map(product -> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND));
                    return mapper.toResponse(cat);
                })
                .collect(Collectors.toList());

        return PageResponse.<VariantAttributeResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public VariantAttributeResponse update(Integer id, UpdateVariantAttributeRequest request) {
        VariantAttribute variantAttribute =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        mapper.update(variantAttribute, request);
        return mapper.toResponse(repo.save(variantAttribute));
    }

    public void delete(Integer id) {
        VariantAttribute variantAttribute =
                repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        repo.delete(variantAttribute);
    }
}
