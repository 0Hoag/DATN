package com.fpl.datn.service.build;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.models.Category;
import com.fpl.datn.repository.CategoryRepository;

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
    CategoryRepository repo;

    public boolean wouldCreateCircularReference(Integer categoryId, Integer parentId) {
        if (categoryId.equals(parentId)) return true;

        Set<Integer> visited = new HashSet<>();
        visited.add(categoryId);

        Integer currentParentId = parentId;
        while (currentParentId != null) {
            if (visited.contains(currentParentId)) return true;

            Category parent =
                    repo.findById(currentParentId).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            if (parent.getParent() == null) break;

            currentParentId = parent.getParent().getId();
            visited.add(currentParentId);
        }

        return false;
    }
}
