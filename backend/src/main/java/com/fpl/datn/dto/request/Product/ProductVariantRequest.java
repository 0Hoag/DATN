package com.fpl.datn.dto.request.Product;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantRequest {
    private Integer id; // mới thêm

    private String variantName;
    @NotBlank(message = "Giá không được để trống")
    private BigDecimal price;
    @NotBlank(message = "Tồn kho không được để trống")
    private Integer quantity;
    @NotBlank(message = "Số lượng bán không được để trống")
    private Integer sold;
    @NotBlank(message = "Mục hoạt động không được để trống")
    private Boolean isActive;
    @NotBlank(message = "ID sản phẩm không được để trống")
    private Integer productId; // ID sản phẩm cha

    private List<Integer> attributeValueIds;   // 🆕 danh sách ID thuộc tính
    List<ProductImageRequest> images;
}
