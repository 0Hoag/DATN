package com.fpl.datn.repository;

import com.fpl.datn.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
