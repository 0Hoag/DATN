package com.fpl.datn.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "product_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Integer rating; // Nullable cho bình luận trả lời của admin
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "is_visible")
    Boolean isVisible = true;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    // Mối quan hệ tự tham chiếu cho bình luận trả lời
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to") // Tên cột là reply_to
    ProductReview parentReview; // Bình luận gốc mà bình luận này trả lời

    // Mối quan hệ một-một cho bình luận trả lời của admin
    @OneToOne(mappedBy = "parentReview", cascade = CascadeType.ALL, orphanRemoval = true)
    ProductReview adminReply; // Bình luận trả lời của admin cho bình luận gốc này

    @OneToOne
    @JoinColumn(name = "order_detail_id", unique = true)
    private OrderDetail orderDetail;
}
