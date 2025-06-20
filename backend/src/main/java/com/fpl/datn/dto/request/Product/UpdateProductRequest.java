package com.fpl.datn.dto.request.Product;

import java.util.List;

import com.fpl.datn.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {
    String name;
    String slug;
    String description;
    String brand;
    String thumbnail;
    String content;
    Boolean isHome;
    Boolean isActive;
    Integer category;
}
