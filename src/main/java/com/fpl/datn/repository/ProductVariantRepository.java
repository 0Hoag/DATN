package com.fpl.datn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ProductVariant;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    boolean existsBySku(String sku);

    List<ProductVariant> findAllByProduct_Id(Integer productId);
}
