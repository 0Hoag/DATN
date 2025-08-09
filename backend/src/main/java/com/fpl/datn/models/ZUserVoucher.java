package com.fpl.datn.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_vouchers")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZUserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed;

    // Thêm columnDefinition để đặt giá trị mặc định là CURRENT_TIMESTAMP
    @Column(name = "assigned_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedAt;
}
