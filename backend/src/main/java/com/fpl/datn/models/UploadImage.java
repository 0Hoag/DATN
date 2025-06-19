package com.fpl.datn.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "upload_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;

    private String fileName;
}
