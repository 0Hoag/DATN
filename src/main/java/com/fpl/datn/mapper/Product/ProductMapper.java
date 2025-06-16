package com.fpl.datn.mapper.Product;

import com.fpl.datn.dto.request.Product.ProductRequest;
import com.fpl.datn.dto.request.Product.UpdateProductRequest;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.mapper.DateMapper;
import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DateMapper.class , ProductVariantMapper.class})
public interface ProductMapper {

    // Entity -> Response
    @Mapping(target = "productReviews", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    ProductResponse toProductResponse(Product product);

    // Request DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "productReviews", ignore = true)
    @Mapping(target = "category", source = "category") // map Integer -> Category
    Product toProduct(ProductRequest request);

    // Update existing entity from DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "productReviews", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "category", source = "category") // map Integer -> Category
    void updateProduct(@MappingTarget Product product, UpdateProductRequest request);

    // Hàm map Integer -> Category (dùng cho cả create và update)
    default Category map(Integer id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
