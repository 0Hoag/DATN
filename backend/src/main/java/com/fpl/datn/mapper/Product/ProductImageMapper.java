package com.fpl.datn.mapper.Product;

import com.fpl.datn.dto.request.Product.ProductImageRequest;
import com.fpl.datn.dto.request.Product.UpdateProductImageRequest;
import com.fpl.datn.dto.response.Product.ProductImageResponse;
import com.fpl.datn.mapper.DateMapper;
import com.fpl.datn.models.ProductImage;
import com.fpl.datn.models.ProductVariant;
import com.fpl.datn.models.UploadImage;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface ProductImageMapper {

    // Convert entity -> response
    @Mapping(source = "productVariant.id", target = "productVariantId")
    @Mapping(source = "uploadImage.id", target = "uploadImageId")
    @Mapping(target = "imageUrl", ignore = true)
    ProductImageResponse toResponse(ProductImage productImage);

    // Request -> Entity
    @Mapping(source = "productVariantId", target = "productVariant", qualifiedByName = "mapToProductVariant")
    @Mapping(source = "uploadImageId", target = "uploadImage", qualifiedByName = "mapToUploadImage")
    ProductImage toEntity(ProductImageRequest request);

    // Update entity (chỉ update các field đơn giản)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "uploadImage", ignore = true)
    void update(@MappingTarget ProductImage productImage, UpdateProductImageRequest request);

    // Custom mapping: set imageUrl thủ công
    @AfterMapping
    default void setImageUrl(@MappingTarget ProductImageResponse dto, ProductImage entity) {
        if (entity.getUploadImage() != null) {
            dto.setImageUrl(entity.getUploadImage().getUrl());
        }
    }

    @Named("mapToProductVariant")
    default ProductVariant mapToProductVariant(Integer id) {
        if (id == null) return null;
        ProductVariant pv = new ProductVariant();
        pv.setId(id);
        return pv;
    }

    @Named("mapToUploadImage")
    default UploadImage mapToUploadImage(Integer id) {
        if (id == null) return null;
        UploadImage img = new UploadImage();
        img.setId(id);
        return img;
    }
}
