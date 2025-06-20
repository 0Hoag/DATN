package com.fpl.datn.mapper.Product;

import com.fpl.datn.dto.response.Product.VariantAttributeValueResponse;
import com.fpl.datn.models.VariantAttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantAttributeValueMapper {
    VariantAttributeValueResponse toResponse(VariantAttributeValue entity);
}
