package com.fpl.datn.mapper.Product;

import com.fpl.datn.dto.request.Product.ProductImageRequest;
import com.fpl.datn.dto.request.Product.UpdateProductImageRequest;
import com.fpl.datn.dto.response.Product.ProductImageResponse;
import com.fpl.datn.mapper.DateMapper;
import com.fpl.datn.models.ProductImage;
import com.fpl.datn.models.ProductVariant;

import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface ProductImageMapper {

    // Entity -> Response
    @Mapping(source = "productVariant.id", target = "productVariantId")
    ProductImageResponse toResponse(ProductImage productImage);

    // Request -> Entity
    @Mapping(source = "productVariantId", target = "productVariant", qualifiedByName = "mapToProductVariant")
    ProductImage toEntity(ProductImageRequest request);

    // Update existing entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    void update(@MappingTarget ProductImage productImage, UpdateProductImageRequest request);

    // Custom: map productVariantId -> ProductVariant
    @Named("mapToProductVariant")
    default ProductVariant mapToProductVariant(Integer id) {
        if (id == null) return null;
        ProductVariant variant = new ProductVariant();
        variant.setId(id);
        return variant;
    }
}
