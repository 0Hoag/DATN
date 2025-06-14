package com.fpl.datn.repository;

import com.fpl.datn.models.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantAttributesRepository extends JpaRepository<VariantAttribute, Integer> {
    Optional<VariantAttribute> findByName(String name);
}
