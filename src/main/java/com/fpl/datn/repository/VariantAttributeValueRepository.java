package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.VariantAttributeValue;

@Repository
public interface VariantAttributeValueRepository extends JpaRepository<VariantAttributeValue, Integer> {
    boolean existsByValueIgnoreCaseAndAttribute_Id(String value, Integer attributeId);
}
