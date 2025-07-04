package com.fpl.datn.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer fpId;

    @Column(nullable = false)
    Integer otp;

    @Column(nullable = false)
    Date expirationTime;

    @OneToOne
    User user;
}
