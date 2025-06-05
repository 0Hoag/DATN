package com.fpl.datn.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "variant_attribute_values")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String value;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private VariantAttribute attribute;

    @OneToMany(mappedBy = "attributeValue")
    private List<ProductVariantAttributeValue> productVariantAttributeValues;
}
