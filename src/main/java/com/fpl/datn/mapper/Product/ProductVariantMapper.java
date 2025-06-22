package com.fpl.datn.mapper.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.fpl.datn.dto.request.Product.ProductVariantRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantRequest;
import com.fpl.datn.dto.response.Product.ProductVariantResponse;
import com.fpl.datn.models.*;

@Mapper(
        componentModel = "spring", uses = {ProductImageMapper.class, ProductVariantAttributeValueMapper.class}
)
public interface ProductVariantMapper {

    // Request → Entity
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdToProduct")
    ProductVariant toEntity(ProductVariantRequest request);

    // Entity → Response
    @Mapping(target = "productId", source = "product.id") // ✅ map lại productId
    @Mapping(target = "attributeValues", source = "attributeValues") // <- quan trọng
    @Mapping(target = "images", source = "productImages") // ✅ map lại danh sách ảnh
    ProductVariantResponse toResponse(ProductVariant productVariant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "attributeValues", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    void toUpdate(@MappingTarget ProductVariant productVariant, UpdateProductVariantRequest request);


    @Named("mapProductIdToProduct")
    default Product mapProductIdToProduct(Integer productId) {
        if (productId == null) return null;
        Product p = new Product();
        p.setId(productId);
        return p;
    }
}