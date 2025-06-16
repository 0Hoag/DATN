package com.fpl.datn.repository;

import com.fpl.datn.models.ProductVariantAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantAttributeValueRepository extends JpaRepository<ProductVariantAttributeValue,Integer> {
        boolean existsByProductVariantIdAndAttributeValueId(Integer variantId, Integer valueId);

        List<ProductVariantAttributeValue> findAllByProductVariantId(Integer productVariantId);

}
