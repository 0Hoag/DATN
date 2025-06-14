package com.fpl.datn.service.Product;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.Product.UpdateVariantAttributeRequest;
import com.fpl.datn.dto.request.Product.VariantAttributeRequest;
import com.fpl.datn.dto.response.Product.VariantAttributeResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.Product.VariantAttributeMapper;
import com.fpl.datn.models.VariantAttribute;
import com.fpl.datn.repository.VariantAttributesRepository;
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
public class VariantAttributeService {
    VariantAttributeMapper mapper;
    VariantAttributesRepository repo;

    public Boolean create(VariantAttributeRequest request) {
        // 1. Kiểm tra tồn tại theo name
        VariantAttribute existing = repo.findByName(request.getName()).orElse(null);

        VariantAttribute entity = mapper.toEntity(request);

        if (existing != null) {
            // 2. Gán ID của entity mới bằng ID của cái cũ để Hibernate hiểu là update
            entity.setId(existing.getId());

            // 3. Gán attribute cha cho các value con
            if (entity.getValues() != null) {
                entity.getValues().forEach(val -> val.setAttribute(entity));
            }

            // 4. Xoá các value cũ nếu muốn (tuỳ ý)
            // existing.getValues().clear(); // nếu bạn muốn xoá hết các giá trị cũ
        } else {
            // 5. Trường hợp thêm mới
            if (entity.getValues() != null) {
                entity.getValues().forEach(val -> val.setAttribute(entity));
            }
        }

        repo.save(entity);
        return true;
    }

    public VariantAttributeResponse detail(Integer id) {
        VariantAttribute variantAttribute = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_DETAIL_EXISTED));
        return mapper.toResponse(variantAttribute);
    }

    public List<VariantAttributeResponse> list(){
        return repo.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<VariantAttributeResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repo.findAll(pageable);

        var data = pageData.getContent().stream()
                .map(product -> {
                    var cat = repo.findById(product.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.VARIANT_DETAIL_EXISTED));
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
        VariantAttribute variantAttribute = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_UPDATE_EXISTED));
        mapper.update(variantAttribute, request);
        return mapper.toResponse(repo.save(variantAttribute));
    }
    public void delete(Integer id) {
        VariantAttribute variantAttribute = repo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_DELETE_EXISTED));
        repo.delete(variantAttribute);
    }
}
