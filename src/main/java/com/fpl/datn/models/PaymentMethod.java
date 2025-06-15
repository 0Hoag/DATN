package com.fpl.datn.models;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    // Relationships
    @OneToMany(mappedBy = "paymentMethod")
    private List<Order> orders;

    @OneToMany(mappedBy = "paymentMethod")
    private List<TransactionLog> transactionLogs;
}
