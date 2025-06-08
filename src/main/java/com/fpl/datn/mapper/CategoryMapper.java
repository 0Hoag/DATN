package com.fpl.datn.mapper;

import com.fpl.datn.dto.request.CategoryRequest;
import com.fpl.datn.dto.request.UpdateCategoryRequest;
import com.fpl.datn.dto.response.CategoryResponse;
import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /*──────────── 1. Entity ➜ Response ────────────*/

    // MapStruct gọi các hàm @Named ở dưới để chuyển đổi
    @Mapping(target = "parent",   source = "parent",   qualifiedByName = "parentToString")
    @Mapping(target = "children", source = "children", qualifiedByName = "childrenToStringList")
    @Mapping(target = "products", source = "products", qualifiedByName = "productsToStringList")
    CategoryResponse toCategoryResponse(Category category);

    /*──────────── 2. Request ➜ Entity ────────────*/

    // Trường parent trong request (kiểu Integer) → Category giả có id
    @Mapping(target = "parent",
            expression = "java(mapParentIdToCategory(request.getParent()))")
    Category toCategory(CategoryRequest request);

    /*──────────── 3. Update Entity từ DTO ────────────*/
    void update(@MappingTarget Category entity, UpdateCategoryRequest update);

    /*──────────── 4. Helpers (custom mapping) ────────────*/

    // Convert parent Category ➜ String
    @Named("parentToString")
    default String parentToString(Category parent) {
        return parent != null ? parent.getName() : null;
    }

    // Convert List<Category> children ➜ List<String>
    @Named("childrenToStringList")
    default List<String> childrenToStringList(List<Category> children) {
        return children == null
                ? Collections.emptyList()
                : children.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    // Convert List<Product> ➜ List<String>
    @Named("productsToStringList")
    default List<String> productsToStringList(List<Product> products) {
        return products == null
                ? Collections.emptyList()
                : products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    // Map parentId (Integer) ➜ Category entity stub
    default Category mapParentIdToCategory(Integer parentId) {
        if (parentId == null) return null;
        Category parent = new Category();
        parent.setId(parentId);
        return parent;
    }
}
