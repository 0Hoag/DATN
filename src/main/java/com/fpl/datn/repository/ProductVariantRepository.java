package com.fpl.datn.repository;

import com.fpl.datn.models.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {
    boolean existsBySku(String sku);
    List<ProductVariant> findAllByProductId(Integer productId);

}
