package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Integer id);
}
