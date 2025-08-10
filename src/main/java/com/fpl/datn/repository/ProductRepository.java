package com.fpl.datn.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.Product.ProductSaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Product;

import feign.Param;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    boolean existsBySlugAndIdNot(String slug, Integer id);

    @Query("""
    SELECT p FROM Product p 
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.slug) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    Page<Product> searchByNameOrSlug(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@org.springframework.data.repository.query.Param("id") Integer id);

    @Query("""
    SELECT new com.fpl.datn.dto.response.Product.ProductSaleResponse(
        p.id, p.name, p.slug, p.thumbnail, MIN(pv.price), MIN(pv.salePrice)
    )
    FROM Product p
    JOIN p.productVariants pv
    WHERE pv.salePrice IS NOT NULL AND pv.salePrice < pv.price
    GROUP BY p.id, p.name, p.slug, p.thumbnail
""")
    Page<ProductSaleResponse> findSaleProductsSimple(Pageable pageable);

    @Query("""
    SELECT new com.fpl.datn.dto.response.Product.ProductSaleResponse(
        p.id,
        p.name,
        p.slug,
        (SELECT MIN(pv.price) FROM ProductVariant pv WHERE pv.product = p),
        (SELECT MIN(pv.salePrice) FROM ProductVariant pv WHERE pv.product = p),
        p.thumbnail,
        (SELECT AVG(r.rating) FROM ProductReview r WHERE r.product = p)
    )
    FROM Product p
    WHERE (:categoryIds IS NULL OR p.category.id IN :categoryIds)
      AND (:brands IS NULL OR p.brand IN :brands)
      AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND EXISTS (
          SELECT 1 FROM ProductVariant pv
          WHERE pv.product = p
          AND (:minPrice IS NULL OR pv.price >= :minPrice)
          AND (:maxPrice IS NULL OR pv.price <= :maxPrice)
      )
""")
    Page<ProductSaleResponse> filterProducts(
            @Param("categoryIds") List<Integer> categoryIds,
            @Param("brands") List<String> brands,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("keyword") String keyword,
            Pageable pageable
    );



    Optional<Product> findBySlug(String slug);

}
