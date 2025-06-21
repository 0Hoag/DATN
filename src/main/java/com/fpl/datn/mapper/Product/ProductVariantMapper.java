package com.fpl.datn.mapper.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.fpl.datn.dto.request.Product.ProductVariantRequest;
import com.fpl.datn.dto.request.Product.UpdateProductVariantRequest;
import com.fpl.datn.dto.response.Product.ProductVariantResponse;
import com.fpl.datn.models.*;

@Mapper(
        componentModel = "spring",
        uses = {ProductImageMapper.class, ProductVariantAttributeValueMapper.class})
public interface ProductVariantMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "attributeValues", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    ProductVariant toEntity(ProductVariantRequest request);

    // Entity → Response
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productImages", source = "productImages")
    @Mapping(target = "attributeValues", source = "attributeValues")
    ProductVariantResponse toResponse(ProductVariant productVariant);

    // Update (Request → Existing Entity)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProductIdToProduct")
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "attributeValues", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    void toUpdate(@MappingTarget ProductVariant productVariant, UpdateProductVariantRequest request);

    // Helpers
    default List<String> mapImages(List<ProductImage> images) {
        if (images == null) return null;
        return images.stream().map(ProductImage::getImageUrl).collect(Collectors.toList());
    }

    default List<String> mapAttributes(List<ProductVariantAttributeValue> values) {
        if (values == null) return null;
        return values.stream()
                .map(v -> {
                    VariantAttribute attr = v.getAttributeValue().getAttribute();
                    return attr.getName() + ": " + v.getAttributeValue().getValue();
                })
                .collect(Collectors.toList());
    }

    // Tạo Product từ ID
    @Named("mapProductIdToProduct")
    default Product mapProductIdToProduct(Integer productId) {
        if (productId == null) return null;
        Product p = new Product();
        p.setId(productId);
        return p;
    }
}
