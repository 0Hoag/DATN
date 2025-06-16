package com.fpl.datn.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.fpl.datn.dto.request.CategoryRequest;
import com.fpl.datn.dto.request.UpdateCategoryRequest;
import com.fpl.datn.dto.response.CategoryResponse;
import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parent", source = "parent", qualifiedByName = "parentToId")
    @Mapping(target = "children", source = "children", qualifiedByName = "childrenToNameList")
    @Mapping(target = "products", source = "products", qualifiedByName = "productsToNameList")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "parent", expression = "java(mapParentIdToCategory(request.getParent()))")
    Category toCategory(CategoryRequest request);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "isShow", source = "isShow")
    void update(@MappingTarget Category category, UpdateCategoryRequest request);

    default void updateParent(@MappingTarget Category category, int parentId) {
        if (parentId == 0) {
            category.setParent(null);
        } else {
            Category parent = new Category();
            parent.setId(parentId);
            category.setParent(parent);
        }
    }

    @Named("parentToId")
    default Integer parentToId(Category parent) {
        return parent != null ? parent.getId() : null;
    }

    @Named("childrenToNameList")
    default List<String> childrenToNameList(List<Category> children) {
        return children == null
                ? Collections.emptyList()
                : children.stream().map(Category::getName).collect(Collectors.toList());
    }

    @Named("productsToNameList")
    default List<String> productsToNameList(List<Product> products) {
        return products == null
                ? Collections.emptyList()
                : products.stream().map(Product::getName).collect(Collectors.toList());
    }

    default Category mapParentIdToCategory(Integer parentId) {
        if (parentId == null || parentId == 0) return null;
        Category parent = new Category();
        parent.setId(parentId);
        return parent;
    }
}
