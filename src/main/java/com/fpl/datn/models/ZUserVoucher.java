package com.fpl.datn.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZUserVoucher {
    // tại sao lại để chữ Z hỏi Hoàng
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
}
