package com.fpl.datn.dto.response.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageResponse {
    private Integer id;
    private String url;
    private String fileName;
    private String publicId;
}
