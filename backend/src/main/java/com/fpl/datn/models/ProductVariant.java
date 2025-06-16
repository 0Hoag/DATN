package com.fpl.datn.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sku;

    @Column(name = "variant_name")
    private String variantName;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private Integer quantity;

    private Integer sold;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    private List<ProductVariantAttributeValue> attributeValues;

    @OneToMany(mappedBy = "productVariant")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "productVariant")
    private List<CartItem> cartItems;
}
