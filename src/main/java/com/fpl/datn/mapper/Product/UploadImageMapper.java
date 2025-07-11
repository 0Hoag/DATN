package com.fpl.datn.mapper.Product;

import java.util.List;

import org.mapstruct.*;

import com.fpl.datn.dto.response.Product.UploadImageResponse;
import com.fpl.datn.models.UploadImage;

@Mapper(componentModel = "spring")
public interface UploadImageMapper {
    UploadImageResponse toResponse(UploadImage image);

    List<UploadImageResponse> toResponse(List<UploadImage> images);
}
