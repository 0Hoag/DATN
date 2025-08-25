package com.fpl.datn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    boolean existsBySlug(String slug);

    Page<Category> findAllByIsShowTrue(Pageable pageable);

    List<Category> findAllByIsShowTrue();

    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    Optional<Category> findBySlug(String slug);
}
