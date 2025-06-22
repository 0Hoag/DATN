package com.fpl.datn.dto.request.Product;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductImageRequest {
    @NotNull(message = "ID ảnh không được để trống")
    Integer id;

    String altText;

    String specDescription;

    @NotNull(message = "Trạng thái ảnh đại diện không được để trống")
    Boolean isThumbnail;

    @NotNull(message = "Thứ tự ảnh không được để trống")
    Integer sortOrder;

    @NotNull(message = "ID ảnh upload không được để trống")
    Integer imageUrl;
}