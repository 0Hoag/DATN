package com.fpl.datn.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "spec_description", columnDefinition = "TEXT")
    private String specDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ✅ Quan hệ với biến thể sản phẩm
    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;
}
