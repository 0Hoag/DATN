package com.fpl.datn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Product;

import feign.Param;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySlug(String slug);
    boolean existsByName(String name);
    boolean existsBySlugAndIdNot(String slug, Integer id);
    // Tìm theo tên hoặc SKU (có chứa chuỗi)
    @Query("SELECT p FROM Product p JOIN p.productVariants v\n"
            + "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))\n"
            + "   OR LOWER(v.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))\n")
    List<Product> searchByNameOrSku(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@org.springframework.data.repository.query.Param("id") Integer id);
}