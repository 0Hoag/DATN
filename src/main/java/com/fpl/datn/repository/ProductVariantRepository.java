package com.fpl.datn.repository;

import java.util.List;
import java.util.Optional;

import feign.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ProductVariant;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    boolean existsBySku(String sku);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId")
    List<ProductVariant> findVariantsByProductId(@Param("productId") Integer productId);
    boolean existsBySkuAndIdNot(String sku, Integer id);
}