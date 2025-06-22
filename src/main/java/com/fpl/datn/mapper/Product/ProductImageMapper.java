package com.fpl.datn.mapper.Product;

import com.fpl.datn.dto.request.Product.ProductImageRequest;
import com.fpl.datn.dto.request.Product.UpdateProductImageRequest;
import com.fpl.datn.dto.response.Product.ProductImageResponse;
import com.fpl.datn.mapper.DateMapper;
import com.fpl.datn.models.ProductImage;
import com.fpl.datn.models.ProductVariant;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface ProductImageMapper {

    // ✅ Response: map productVariantId & giữ nguyên imageUrl
    @Mapping(source = "productVariant.id", target = "productVariantId")
    ProductImageResponse toResponse(ProductImage productImage);

    List<ProductImageResponse> toResponseList(List<ProductImage> images);

    // ✅ Create: map tất cả, kể cả imageUrl
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "productVariant", source = "productVariantId", qualifiedByName = "toVariant")
    ProductImage toEntity(ProductImageRequest request);

    // ✅ Update: cho phép cập nhật imageUrl
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void update(@MappingTarget ProductImage productImage, UpdateProductImageRequest request);

    @Named("toVariant")
    default ProductVariant toVariant(Integer id) {
        if (id == null) return null;
        ProductVariant pv = new ProductVariant();
        pv.setId(id);
        return pv;
    }
}