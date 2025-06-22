package com.fpl.datn.mapper.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.fpl.datn.dto.request.Product.ProductVariantAttributeValueRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantAttributeValueRequest;
import com.fpl.datn.dto.response.Product.ProductVariantAttributeValueResponse;
import com.fpl.datn.models.ProductVariantAttributeValue;

@Mapper(componentModel = "spring")
public interface ProductVariantAttributeValueMapper {

    // Create
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "productVariantId", target = "productVariant.id")
    @Mapping(source = "attributeValueId", target = "attributeValue.id")
    ProductVariantAttributeValue toEntity(ProductVariantAttributeValueRequest request);

    // Response
    @Mapping(source = "attributeValue.value", target = "attributeValue")
    @Mapping(source = "attributeValue.attribute.name", target = "attributeName")
    ProductVariantAttributeValueResponse toResponse(ProductVariantAttributeValue entity);

    // ✅ Update - cập nhật entity hiện tại từ request
    @Mapping(source = "productVariantId", target = "productVariant.id")
    @Mapping(source = "attributeValueId", target = "attributeValue.id")
    void update(@MappingTarget ProductVariantAttributeValue entity, UpdateProductVariantAttributeValueRequest request);
}