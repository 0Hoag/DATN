package com.fpl.datn.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpl.datn.dto.response.Product.ProductSaleResponse;
import com.fpl.datn.models.Product;

import feign.Param;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    boolean existsBySlugAndIdNot(String slug, Integer id);

    @Query(
            """
	SELECT p FROM Product p
	WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
	OR LOWER(p.slug) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    Page<Product> searchByNameOrSlug(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@org.springframework.data.repository.query.Param("id") Integer id);

    @Query(
            """
	SELECT new com.fpl.datn.dto.response.Product.ProductSaleResponse(
		p.id, p.name, p.slug, p.thumbnail, MIN(pv.price), MIN(pv.salePrice)
	)
	FROM Product p
	JOIN p.productVariants pv
	WHERE pv.salePrice IS NOT NULL AND pv.salePrice < pv.price
	GROUP BY p.id, p.name, p.slug, p.thumbnail
""")
    Page<ProductSaleResponse> findSaleProductsSimple(Pageable pageable);
}
