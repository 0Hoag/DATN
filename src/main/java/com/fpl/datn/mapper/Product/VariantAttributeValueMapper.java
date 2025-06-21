package com.fpl.datn.mapper.Product;

import org.mapstruct.Mapper;

import com.fpl.datn.dto.response.Product.VariantAttributeValueResponse;
import com.fpl.datn.models.VariantAttributeValue;

@Mapper(componentModel = "spring")
public interface VariantAttributeValueMapper {
    VariantAttributeValueResponse toResponse(VariantAttributeValue entity);
}
