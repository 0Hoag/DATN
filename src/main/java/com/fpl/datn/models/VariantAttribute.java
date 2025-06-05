package com.fpl.datn.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "variant_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String name;

    // Relationships
    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<VariantAttributeValue> values;
}