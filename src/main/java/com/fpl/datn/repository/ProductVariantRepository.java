package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {
    boolean existsBySku(String sku);
    List<ProductVariant> findAllByProduct_Id(Integer productId);
}
