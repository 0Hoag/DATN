package com.fpl.datn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ProductVariantAttributeValue;

@Repository
public interface ProductVariantAttributeValueRepository extends JpaRepository<ProductVariantAttributeValue, Integer> {
    boolean existsByProductVariantIdAndAttributeValueId(Integer variantId, Integer valueId);

    List<ProductVariantAttributeValue> findAllByProductVariant_Id(Integer productVariantId);
}
