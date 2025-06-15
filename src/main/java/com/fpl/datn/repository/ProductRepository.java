package com.fpl.datn.repository;

import com.fpl.datn.models.Product;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, Integer id);
    // Tìm theo tên hoặc SKU (có chứa chuỗi)
    @Query("SELECT p FROM Product p JOIN p.productVariants v\n" +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))\n" +
            "   OR LOWER(v.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))\n")
    List<Product> searchByNameOrSku(@Param("keyword") String keyword);
}
