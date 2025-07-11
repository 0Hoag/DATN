package com.fpl.datn.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String brand;

    private String thumbnail;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_home")
    private Boolean isHome;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> productVariants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;
}
