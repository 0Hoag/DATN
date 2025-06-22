package com.fpl.datn.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_logs")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer type;

    // Note: Có typo trong schema "amout" thay vì "amount"
    @Column(name = "amout", precision = 10, scale = 2)
    private BigDecimal amount;

    private String status;

    @Column(name = "transaction_ref", columnDefinition = "TEXT")
    private String transactionRef;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "return_id")
    private OrderReturn orderReturn;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
}
