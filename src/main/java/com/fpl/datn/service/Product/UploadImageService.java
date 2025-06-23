package com.fpl.datn.service.Product;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpl.datn.configuration.CloudinaryConfig;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.Product.UploadImageResponse;
import com.fpl.datn.mapper.Product.UploadImageMapper;
import com.fpl.datn.models.UploadImage;
import com.fpl.datn.repository.UploadImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImageService {

    private final Cloudinary cloudinary;
    private final UploadImageRepository uploadImageRepo;
    private final UploadImageMapper mapper;
    private final CloudinaryConfig cloudinaryConfig ;

    // Upload một ảnh đơn
    public UploadImage upload(MultipartFile file) {
    	
        try {
            Map<?, ?> result =
                    cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap());
            UploadImage image = UploadImage.builder()
                    .url((String) result.get("secure_url"))
                    .publicId((String) result.get("public_id"))
                    .fileName(file.getOriginalFilename())
                    .build();
            return uploadImageRepo.save(image);
        } catch (IOException e) {
            log.error("Upload failed", e);
            throw new RuntimeException("Upload to Cloudinary failed");
        }
    }

    public List<UploadImage> uploadMultiple(List<MultipartFile> files) {
        return files.stream().map(this::upload).collect(Collectors.toList());
    }

    public UploadImage getById(Integer id) {
        return uploadImageRepo.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public void delete(List<Integer> id) throws IOException {
        List<UploadImage> images = uploadImageRepo.findAllById(id);
        for (UploadImage image : images) {
            cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
        }
        uploadImageRepo.deleteAll(images);
    }

    public List<UploadImageResponse> list() {
        List<UploadImage> images = uploadImageRepo.findAll();
        return mapper.toResponse(images);
    }

    public PageResponse<UploadImageResponse> get(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = uploadImageRepo.findAll(pageable);

        var data = pageData.getContent().stream().map(mapper::toResponse).collect(Collectors.toList());

        return PageResponse.<UploadImageResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }
}
