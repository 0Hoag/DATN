package com.fpl.datn.mapper;

import java.util.Collections;
import java.util.List;
import org.mapstruct.*;
import com.fpl.datn.dto.request.Category.CategoryRequest;
import com.fpl.datn.dto.request.Category.CategoryUpdateRequest;
import com.fpl.datn.dto.response.products.CategoryResponse;
import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "mapParentCategory")
    Category toCategory(CategoryRequest request);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", source = "products", qualifiedByName = "mapProducts")
    CategoryResponse toCategoryResponse(Category entity);

    @Mapping(target = "parent", source = "parent", qualifiedByName = "mapParentCategory")
    void updateCategory(@MappingTarget Category entity, CategoryUpdateRequest request);

    @Named("mapParentCategory")
    default Category mapParentCategory(String parentName) {
        if (parentName == null || parentName.isEmpty()) return null;
        Category parent = new Category();
        parent.setName(parentName);
        return parent;
    }

    @Named("mapChildrenCategories")
    default List<Category> mapChildrenCategories(List<Category> children) {
        if (children == null) return Collections.emptyList();
        return children;
    }

    @Named("mapProducts")
    default List<Product> mapProducts(List<Product> products) {
        if (products == null) return Collections.emptyList();
        return products;
    }

}
