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
    private String url; // Đường dẫn tuyệt đối đến ảnh (VD: https://cdn.example.com/...)

    private String fileName; // Tên file gốc (dùng hiển thị, debug, tìm kiếm,...)
}
