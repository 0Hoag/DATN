package com.fpl.datn.mapper.Product;

import com.fpl.datn.mapper.ProductReviewMapper;
import com.fpl.datn.models.ProductVariant;
import org.mapstruct.*;

import com.fpl.datn.dto.request.Product.ProductRequest;
import com.fpl.datn.dto.request.Product.UpdateProductRequest;
import com.fpl.datn.dto.response.Product.ProductResponse;
import com.fpl.datn.mapper.CategoryMapper;
import com.fpl.datn.mapper.DateMapper;
import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(
        componentModel = "spring",
        uses = {DateMapper.class, ProductVariantMapper.class, CategoryMapper.class, ProductReviewMapper.class})
public interface ProductMapper {

    // Entity -> Response
    ProductResponse toProductResponse(Product product);

    // Request DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryIdToCategory")
    @Mapping(target = "productVariants", ignore = true)
    Product toProduct(ProductRequest request);

    // Update existing entity from DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryIdToCategory")
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

    @Named("mapCategoryIdToCategory")
    default Category mapCategoryIdToCategory(Integer categoryId) {
        if (categoryId == null) return null;
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }


    // Các phương thức mặc định để tính giá
    default BigDecimal getMinPrice(Product product) {
        return product.getProductVariants().stream()
                .map(ProductVariant::getPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(null);
    }

    default BigDecimal getMinSalePrice(Product product) {
        return product.getProductVariants().stream()
                .map(ProductVariant::getSalePrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(null);
    }


    default BigDecimal getFinalPrice(Product product) {
        BigDecimal minSale = getMinSalePrice(product);
        return (minSale != null) ? minSale : getMinPrice(product);
    }
}
