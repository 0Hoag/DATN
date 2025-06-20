package com.fpl.datn.service.build;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.repository.CategoryRepository;
import com.fpl.datn.models.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@Service
@FieldDefaults(makeFinal = true)
public class CategoryBuilder {
    CategoryRepository categoryRepository;

    public boolean wouldCreateCircularReference(Integer categoryId, Integer newParentId) {
        if (categoryId == null || newParentId == null) return false;
        if (categoryId.equals(newParentId)) return true;

        Set<Integer> visited = new HashSet<>();
        Integer current = newParentId;

        while (current != null) {
            if (!visited.add(current)) break;

            if (current.equals(categoryId)) {
                return true;
            }

            Category parent = categoryRepository.findById(current).orElse(null);
            if (parent == null) break;

            current = parent.getParent() != null ? parent.getParent().getId() : null;
        }

        return false;
    }
}